package org.apache.shiro.authc;

public class JoinAuthenticationToken implements HostAuthenticationToken, RememberMeAuthenticationToken {
    
    private String username;
    private String token;
    private boolean rememberMe;
    private String host;
    private String password;
    
    public JoinAuthenticationToken() {
        this.rememberMe = false;
    }
    
    public JoinAuthenticationToken(String username, String token) {
        this(username, token, false, (String) null);
    }
    
    
    public JoinAuthenticationToken(String username, String token, String host) {
        this(username, token, false, host);
    }
    
    
    public JoinAuthenticationToken(String username, String token, boolean rememberMe, String host) {
        this.username = username;
        this.token = token;
        this.rememberMe = rememberMe;
        this.host = host;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Object getPrincipal() {
        return this.getUsername();
    }
    
    public Object getCredentials() {
        return this.password.toCharArray();
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public boolean isRememberMe() {
        return this.rememberMe;
    }
    
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void clear() {
        this.username = null;
        this.host = null;
        this.rememberMe = false;
        this.token = null;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(this.username);
        sb.append(", rememberMe=").append(this.rememberMe);
        if (this.host != null) {
            sb.append(" (").append(this.host).append(")");
        }
        
        return sb.toString();
    }
}
