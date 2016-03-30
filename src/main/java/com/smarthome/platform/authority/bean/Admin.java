package com.smarthome.platform.authority.bean;

import java.util.Date;

public class Admin {
    private String userId;

    private String realName;

    private String password;

    private Integer enable;

    private Date createTime;

    private String description;
    
    private Date startTime;
    
    private Date endTime;
    
    private String roles;
    /**
     * 管理员邮箱
     */
    private String email;
    /**
     * 管理员电话
     */
    private String phone;
    /**
     * 是否接收告警。1接收  0不接收
     */
    private Integer enable_alert;
    /**
     * 在zabbix中的userID
     */
    private String zabbix_id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getEnable_alert() {
		return enable_alert;
	}

	public void setEnable_alert(Integer enable_alert) {
		this.enable_alert = enable_alert;
	}

	public String getZabbix_id() {
		return zabbix_id;
	}

	public void setZabbix_id(String zabbix_id) {
		this.zabbix_id = zabbix_id;
	}
    
    
}