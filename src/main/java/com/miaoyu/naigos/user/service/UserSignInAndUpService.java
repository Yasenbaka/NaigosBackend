package com.miaoyu.naigos.user.service;

import com.miaoyu.naigos.constantsMap.ErrorMap;
import com.miaoyu.naigos.constantsMap.NormalMap;
import com.miaoyu.naigos.jwtHandle.JwtSigned;
import com.miaoyu.naigos.model.UserArchiveModel;
import com.miaoyu.naigos.model.UserPasswordModel;
import com.miaoyu.naigos.service.AppService;
import com.miaoyu.naigos.user.mapper.GetUserPasswordMapper;
import com.miaoyu.naigos.user.mapper.IsUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

@Service
public class UserSignInAndUpService {
    @Autowired
    private IsUserMapper isUserMapper;
    @Autowired
    private GetUserArchiveService getUserArchiveService;
    @Autowired
    private AppService appService;
    @Autowired
    private GetUserPasswordMapper getUserPasswordMapper;
    @Autowired
    private JwtSigned jwtSigned;

    /**
    * 处理账号是否存在数据库且密码是否正确
    * param
    *   accountType: String 账号的规则
    *   account: String 账号
    *   webPwd: String 网页中用户传来的密码*/
    public Map<String, Object> isUserAndPwdInDatabase(String accountType, String account, String webPwd){
        UserArchiveModel userArchive = null;
        // 选择账号的模式，并获取账号详情信息
        switch (accountType){
            case "id": {
                userArchive = getUserArchiveService.getUserArchive(Integer.parseInt(account));
                break;
            } case "email": {
                userArchive = getUserArchiveService.getUserArchive(1, account);
                break;
            } default: return null;
        }
        if (userArchive != null) {
            if (userArchive.getSafe_level() < 0){
                return null;
            }
            // 有账号并且没有被封禁
            String uuid = userArchive.getGroup_real_user_id();
            // 判断账号的密码是否正确
            Boolean isUserPasswordConsistent = isUserPasswordConsistent(uuid, webPwd);
            if (isUserPasswordConsistent) {
                return new NormalMap().normalSuccessMap(jwtSigned.jwtSigned("web", uuid));
            }
            return null;
        } else {
            return null;
        }
    }

    /**
    * 处理账号是否存在并签发验证码
    * param
    *   accountType: String 账号规则
    *   account: String 账号*/
    public String findArchiveAndCode(String accountType, String account){
        UserArchiveModel userArchive = null;
        switch (accountType){
            case "id": {
                userArchive = getUserArchiveService.getUserArchive(Integer.parseInt(account));
                break;
            } case "email": {
                userArchive = getUserArchiveService.getUserArchive(1, account);
                break;
            } default: return null;
        }
        // 当账号存在
        if (userArchive != null){
            String uuid = userArchive.getGroup_real_user_id();
            UserPasswordModel userPasswordTable = getUserPasswordMapper.getUserPasswordTable(uuid);
            String gc = generateCode(); // 生成安全验证码
            if (userPasswordTable != null){
                // 若数据库中存在密码表
                boolean b = getUserPasswordMapper.updateUserPasswordCode(
                        uuid, gc, System.currentTimeMillis() + (3600 * 1000 * 24));
                if (b){
                    return gc;
                } else {
                    return null;
                }
            }
            // 若数据库中不存在密码表则创建密码表并直接将验证码写入
            boolean userPasswordRecodeWithCode = getUserPasswordMapper.createUserPasswordRecodeWithCode(
                    uuid, gc, System.currentTimeMillis() + (3600 * 1000 * 24));
            if (!userPasswordRecodeWithCode) {
                return gc;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 服务层 确认无密码登录
     * param
     *  userArchive: UserArchiveModel 用户的账号详情信息
     *  code: String 验证码
     * */
    public Map<String, Object> nopwdSignin(UserArchiveModel userArchive, String code){
        if (userArchive != null) {
            String uuid = userArchive.getGroup_real_user_id();
            System.out.println("uuid" + uuid);
            UserPasswordModel userPasswordTable = getUserPasswordMapper.getUserPasswordTable(uuid);
            if (userPasswordTable != null) {
                // 存在密码表
                String dbCode = userPasswordTable.getCode();
                if (dbCode == null) return new ErrorMap().errorMap("没有验证码");
                // 存在验证码
                boolean dbIsCode = userPasswordTable.isIs_code();
                long dbExpirationDate = userPasswordTable.getExpiration_date();
                if (dbCode.equals(code) && dbIsCode && dbExpirationDate > System.currentTimeMillis()) {
                    // 验证码相等 验证码已经被验证 验证码记录时间戳未过期
                    boolean b = getUserPasswordMapper.checkUserCodeSignin(uuid); //删除验证码 重置被验证 删除时间戳
                    if (b) {
                        return new NormalMap().normalSuccessMap(jwtSigned.jwtSigned("web", uuid));
                    }
                    return new ErrorMap().errorMap("签发失败");
                }
                return new ErrorMap().errorMap("数据错误");
            }
            return new ErrorMap().errorMap("没有验证码数据");
        }
        return new ErrorMap().errorMap("ID不存在");
    }
    // 私有 判断密码哈希值是否相等
    private Boolean isUserPasswordConsistent(String uuid, String webPwd){
        String dbPwd;
        UserPasswordModel userPasswordInDatabase = isUserMapper.isUserPasswordInDatabase(uuid);
        dbPwd = userPasswordInDatabase.getPassword();
        String hashPwd = passwordHash(webPwd);
        System.out.println("ServerSavePwd:" + dbPwd + "\nHashPwd:" + hashPwd);
        return dbPwd != null && dbPwd.equals(hashPwd);
    }
    // 私有 根据加盐值和SHA256计算密码的哈希值
    private String passwordHash(String target){
        String pwdKey = appService.getPwdKey();
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = target + pwdKey;
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b: encodedHash){
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e){
            return null;
        }
    }
    // 私有 生成安全验证码
    private String generateCode(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomNum = random.nextInt(10);
            sb.append(randomNum);
        }
        return sb.toString();
    }
}