package com.smarthome.platform.authority.dao.mysql;

import java.util.List;
import java.util.Map;

import com.smarthome.platform.authority.bean.Admin;

public interface AdminDao {
	/**
	 * 根据用户Id号删除管理员用户
	 * @param userId
	 * @return
	 */
    int deleteById(String userId);
    /**
     * 删除用户对应的用户角色表
     * @param userId 用户Id号
     * @return
     */
    int deleteUserRoleById(String userId);
    
    int insert(Admin record);

    /**
     * 添加管理员用户
     * @param record
     * @return
     */
    int addAdmin(Admin record);
    /**
     * 添加管理员对应的角色信息
     * @param map
     * @return
     */
    int addAdminWithRole(Map<String,Object> map);
    /**
     * 根据条件分页查询用户的信息
     * @param map
     * @return
     */
    List<Admin> getUserPageByParams(Map<String,Object> map);
    /**
     * 查询符合条件用户的总数目
     * @param map
     * @return
     */
    int getUserCountByParams(Map<String,Object> map);
    

    Admin getAdminById(String userId);

    int updateAdminByBean(Admin record);
}