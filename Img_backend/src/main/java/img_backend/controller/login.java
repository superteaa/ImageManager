package img_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import java.sql.*;

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

@RestController
public class login {
    private String username;
    private String password;
    private Connection cnn;

    @Autowired
    private db_cnn dbConn;
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String request_logup(@RequestBody LogupRequest body) {
        username = body.getUsername();
        password = body.getPassword();
        PreparedStatement pstm = null;
        try {
            cnn = dbConn.connect();
            String checkSQL = "SELECT username FROM user WHERE username = ? and password = ?";
            pstm = cnn.prepareStatement(checkSQL);
            pstm.setString(1, username);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();
            if(!rs.next()){
                cnn.close();
                return "用户名或密码出错！";
            }
            else {
                cnn.close();
                return "登陆成功！";
            }
        }
        catch (SQLException e){
            return ("Error: " + e.getMessage());
        }
    }
}
