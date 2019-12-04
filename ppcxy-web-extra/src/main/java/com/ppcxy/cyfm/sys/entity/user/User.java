package com.ppcxy.cyfm.sys.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.repository.jpa.support.annotation.EnableQueryCache;
import com.ppcxy.cyfm.sys.entity.permission.Role;
import com.ppcxy.cyfm.sys.entity.team.Team;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Collections3;
import org.springside.modules.utils.Encodes;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户.
 *
 * @author calvin
 */
@Entity
@Table(name = "cy_sys_user")
@EnableQueryCache
// 默认的缓存策略.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity {
    public static final String USERNAME_PATTERN = "^[\\u4E00-\\u9FA5\\uf900-\\ufa2d_a-zA-Z][\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w]{1,19}$";
    public static final String EMAIL_PATTERN = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?";
    public static final String MOBILE_PHONE_NUMBER_PATTERN = "^0{0,1}(13[0-9]|15[0-9]|14[0-9]|18[0-9])[0-9]{8}$";
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 20;
    private static final int SALT_SIZE = 8;
    private static final String PASSWORD_PATTREN = "(.{0}|^[a-zA-Z\\d_]{4,20}$)";
    private String username;
    private String plainPassword;
    private String password;
    private String salt;
    private String name;
    private String email;
    private String tel;
    private UserStatus status;
    private Date createDate;
    
    private Team team;
    
    private List<Role> roleList = Lists.newArrayList(); // 有序的关联对象集合
    //TOTP认证密匙
    private String totpSecret;
    
    
    @NotNull(message = "{not.null}")
    @Pattern(regexp = USERNAME_PATTERN, message = "{user.username.not.valid}")
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Transient
    @JsonIgnore
    @Pattern(regexp = PASSWORD_PATTREN, message = "{user.password.not.valid}")
    public String getPlainPassword() {
        return plainPassword;
    }
    
    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @JsonIgnore
    public String getSalt() {
        return salt;
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    @NotBlank
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Email
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    @Enumerated(EnumType.STRING)
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public String getTotpSecret() {
        return totpSecret;
    }
    
    public void setTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
    }
    
    @JsonIgnore
    // 多对多定义
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cy_sys_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    // Fecth策略定义
    @Fetch(FetchMode.SELECT)
    @Basic(optional = true, fetch = FetchType.EAGER)
    // 集合按id排序
    @OrderBy("id ASC")
    // 缓存策略
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Role> getRoleList() {
        return roleList;
    }
    
    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "team_id")
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    @Transient
    @JsonIgnore
    public String getRoleNames() {
        return Collections3.extractToString(roleList, "name", ", ");
    }
    
    @Transient
    @JsonIgnore
    public String getRoleValues() {
        return Collections3.extractToString(roleList, "value", ", ");
    }
    
    public void randomSalt() {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        this.setSalt(Encodes.encodeHex(salt));
    }
    
    /**
     * 显示用户名用户姓名的组合,格式:用户姓名[用户名]
     *
     * @return
     */
    @Transient
    public String getShowName() {
        return String.format("%s[%s]", this.getName(), this.getUsername());
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", team=" + team.getName() +
                ", roleList=" + Collections3.convertToString(roleList, "name", ",") +
                '}';
    }
    
    
}
