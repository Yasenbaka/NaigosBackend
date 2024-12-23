package com.miaoyu.naigos.user.service;

import com.miaoyu.naigos.constantsMap.ErrorMap;
import com.miaoyu.naigos.constantsMap.NormalMap;
import com.miaoyu.naigos.model.UserArchiveModel;
import com.miaoyu.naigos.model.UserPermiModel;
import com.miaoyu.naigos.user.mapper.GetUserArchiveMapper;
import com.miaoyu.naigos.userPermi.PermiConst;
import com.miaoyu.naigos.userPermi.UserPermi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetUserArchiveService {
    @Autowired
    private GetUserArchiveMapper getUserArchiveMapper;

    /**
     * 根据UID获取用户信息
     * param
     *  uniqueId: Integer 唯一UID*/
    public UserArchiveModel getUserArchive(Integer uniqueId) {
        return getUserArchiveMapper.getUserArchiveByUniqueId(uniqueId);
    }
    /**
     * 根据账号规则获取用户信息
     * param
     *  loginType: int 账号规则 1:email 2:uuid
     *  uid: String 账号*/
    public UserArchiveModel getUserArchive(int loginType, String uid) {
        return switch (loginType) {
            case 1 -> getUserArchiveMapper.getUserArchiveByEmail(uid);
            case 2 -> getUserArchiveMapper.getUserArchiveByUuid(uid);
            case 3 -> getUserArchiveMapper.getUserArchiveByQqId(Long.valueOf(uid));
            default -> null;
        };
    }

    /**
     * 获取用户的头像
     * @param uuid 用户的唯一识别符
     * @return Map->JSON
     * */
    public Map<String, Object> getMeAvatarService(String uuid){
        UserArchiveModel userArchiveByUuid = getUserArchiveMapper.getUserArchiveByUuid(uuid);
        if (userArchiveByUuid == null) {
            return new ErrorMap().errorMap("未找到用户！");
        }
        String avatarUrl = userArchiveByUuid.getAvatar();
        return new NormalMap().normalSuccessMap(avatarUrl);
    }

    /**
     * 获取用户积分和好感度
     * @param uuid 用户的唯一识别符
     * @return Map->JSON
     * */
    public Map<String, Object> getMeScoreService(String uuid) {
        UserArchiveModel userArchiveByUuid = getUserArchiveMapper.getUserArchiveByUuid(uuid);
        if (userArchiveByUuid == null) {
            return new ErrorMap().errorMap("未找到用户！");
        }
        int score = userArchiveByUuid.getScore();
        int favorite = userArchiveByUuid.getFavorite();
        Map<String, Integer> dates = new HashMap<String, Integer>();
        dates.put("score", score);
        dates.put("favorite", favorite);
        return new NormalMap().normalSuccessMap(dates);
    }

    @Autowired
    private PermiConst permiConst;
    @Autowired
    private UserPermi userPermi;
    public Map<String, Object> getMePermissionService(String uuid) {
        UserPermiModel userPermiDB = getUserArchiveMapper.getUserPermiByUuid(uuid);
        Map<String, Object> datas = new HashMap<>();
        if (userPermiDB == null) {
            datas.put("permissions", PermiConst.USER);
            datas.put("cn", permiConst.toString(PermiConst.USER));
            return new NormalMap().normalSuccessMap(datas);
        }
        userPermi.setPermissions(userPermiDB.getPermission()); // 挂载用户权限数字
        // 分析权限
        if (userPermi.hasPermission(PermiConst.ADMIN)){
            datas.put("cn", permiConst.toString(PermiConst.ADMIN));
        } else if (userPermi.hasPermission(PermiConst.MANAGER)) {
            datas.put("cn", permiConst.toString(PermiConst.MANAGER));
        } else if (userPermi.hasPermission(PermiConst.DEVELOPER)) {
            datas.put("cn", permiConst.toString(PermiConst.DEVELOPER));
        } else if (userPermi.hasPermission(PermiConst.CREATOR)) {
            datas.put("cn", permiConst.toString(PermiConst.CREATOR));
        } else {
            datas.put("cn", permiConst.toString(PermiConst.USER));
        }
        datas.put("permissions", userPermiDB.getPermission());
        return new NormalMap().normalSuccessMap(datas);
    }
    public Map<String, Object> getMePermissionServiceList(String uuid) {
        UserPermiModel userPermiDB = getUserArchiveMapper.getUserPermiByUuid(uuid);
        List<Map<String, Object>> list = new ArrayList<>();
        if (userPermiDB == null) {
            Map<String, Object> datas = new HashMap<>();
            datas.put("permissions", PermiConst.USER);
            datas.put("cn", permiConst.toString(PermiConst.USER));
            list.add(datas);
            return new NormalMap().normalSuccessMap(list);
        }
        userPermi.setPermissions(userPermiDB.getPermission()); // 挂载用户权限数字
        if (userPermi.hasPermission(PermiConst.ADMIN)){
            Map<String, Object> datas = new HashMap<>();
            datas.put("permissions", PermiConst.ADMIN);
            datas.put("cn", permiConst.toString(PermiConst.ADMIN));
            list.add(datas);
        }
        if (userPermi.hasPermission(PermiConst.MANAGER)){
            Map<String, Object> datas = new HashMap<>();
            datas.put("permissions", PermiConst.MANAGER);
            datas.put("cn", permiConst.toString(PermiConst.MANAGER));
            list.add(datas);
        }
        if (userPermi.hasPermission(PermiConst.DEVELOPER)){
            Map<String, Object> datas = new HashMap<>();
            datas.put("permissions", PermiConst.DEVELOPER);
            datas.put("cn", permiConst.toString(PermiConst.DEVELOPER));
            list.add(datas);
        }
        if (userPermi.hasPermission(PermiConst.CREATOR)){
            Map<String, Object> datas = new HashMap<>();
            datas.put("permissions", PermiConst.CREATOR);
            datas.put("cn", permiConst.toString(PermiConst.CREATOR));
            list.add(datas);
        }
        if (userPermi.hasPermission(PermiConst.USER)){
            Map<String, Object> datas = new HashMap<>();
            datas.put("permissions", PermiConst.USER);
            datas.put("cn", permiConst.toString(PermiConst.USER));
            list.add(datas);
        }
        return new NormalMap().normalSuccessMap(list);
    }
}
