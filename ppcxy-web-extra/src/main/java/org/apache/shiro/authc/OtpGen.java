package org.apache.shiro.authc;

public class OtpGen {
    
    
    public static void main(String[] args) throws Exception {
        //String secret = TotpUtil.getRandomSecretBase32(128);
        //System.out.println(secret);
        System.out.println(TotpUtil.generate("EH5VU5ZP2GXEWXRSOAMAFDSVE6RMH2V7TVZ2P55G4SOYUK3N2LQE4DICGCJXA5Y4DG554AT4T5EIJP2XS45A4WUST6LNZY7CZNBIDJQ"));
    }
    
}
