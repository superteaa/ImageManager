package img_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import java.sql.*;

class LogupRequest {
    private String username;
    private String password;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

@RestController
public class logup {
    private String username;
    private String password;
    private String email;
    private Connection cnn;

    @Autowired
    private db_cnn dbConn;
    @RequestMapping(value = "/logup", method = RequestMethod.POST)
    public String request_logup(@RequestBody LogupRequest body) {
        username = body.getUsername();
        password = body.getPassword();
        email = body.getEmail();
        PreparedStatement pstm = null;
        try {
            cnn = dbConn.connect();
            String checkSQL = "SELECT username FROM user WHERE username = ?";
            pstm = cnn.prepareStatement(checkSQL);
            pstm.setString(1, username);
            ResultSet rs = pstm.executeQuery();
            if(!rs.next()){
                String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
                pstm = cnn.prepareStatement(sql);
                pstm.setString(1, username);
                pstm.setString(2, password);
                pstm.setString(3, email);
                int rs1 = pstm.executeUpdate();
                if (rs1 > 0){
                    cnn.close();
                    return "success";
                }
                else {
                    cnn.close();
                    return "没有数据被插入！";
                }
            }
            else {
                cnn.close();
                return "用户已存在！";
            }
        }
        catch (SQLException e){
            return ("Error: " + e.getMessage());
        }
    }
}
