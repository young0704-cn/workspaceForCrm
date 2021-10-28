package com.home.crm.settings.domain;

public class User {
    /*
        日期格式:yyyy-MM-dd 定长10个字符     yyyy-MM-dd HH:mm:SS 定长19个字符
        定长采用MySQL数据类型设为char提高效率

        登录需求:
            验证账号密码
            <select id="userLogin" parameterType="user" resultType="user">
            sql语句   select * from tbl_user where loginAct=#{} and loginPwd=#{};
            User use = new user();
            user.setLoginAct("xxx");
            user.setLoginPwd("123");
            User result_user = session.select(user);
            if(result_user==null){
                账号密码错误
            }else{
                result_user=null    说明账号密码正确，但还要验证其他信息
                                    如allowIps,lockState,expireTime等
            }
    */
    private String id;          //用户编号      主键
    private String loginAct;    //登录账号
    private String name;        //用户姓名
    private String loginPwd;    //用户密码
    private String email;       //邮箱
    private String expireTime ; //失效时间  yyyy-MM-dd HH:mm:SS
    private String lockState;   //锁定状态  0表示锁住,1表示启用
    private String deptno;      //部门编号
    private String allowIps;    //允许访问的ip
    private String createTime;  //创建时间  yyyy-MM-dd HH:mm:SS
    private String createBy;    //创建人
    private String editTime;    //最后修改时间  yyyy-MM-dd HH:mm:SS
    private String editBy;      //最后修改人  yyyy-MM-dd HH:mm:SS

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", loginAct='" + loginAct + '\'' +
                ", name='" + name + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", email='" + email + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", lockState='" + lockState + '\'' +
                ", deptno='" + deptno + '\'' +
                ", allowIps='" + allowIps + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", editTime='" + editTime + '\'' +
                ", editBy='" + editBy + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}