package com.usersservice.dao.impl;

import com.datastax.driver.core.*;
import com.usersservice.dao.UserDAO;
import com.usersservice.domain.User;
import com.usersservice.util.UserServiceConstants;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private Session session;

    private Boolean isInitialized;
    private PreparedStatement prepareGetAllUsers;
    private PreparedStatement prepareGetUser;
    private PreparedStatement prepareSaveUser;

    public UserDAOImpl(Session session) {
        this.session = session;
        isInitialized = false;
    }

    private void initStatements(){
        prepareGetAllUsers = session.prepare("SELECT * FROM " +
                UserServiceConstants.USERS_TABLE_NAME);

        String queryGetUser = String.format("SELECT * FROM %s WHERE %s = ?", UserServiceConstants.USERS_TABLE_NAME,
                UserServiceConstants.COLUMN_ID);
        prepareGetUser = session.prepare(queryGetUser);

        String querySaveUser = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                UserServiceConstants.USERS_TABLE_NAME, UserServiceConstants.COLUMN_ID,
                UserServiceConstants.COLUMN_USERNAME, UserServiceConstants.COLUMN_PASSWORD,
                UserServiceConstants.COLUMN_EMAIL);
        prepareSaveUser = session.prepare(querySaveUser);

        isInitialized = true;
    }

    public List<User> getAll(){
        if (!isInitialized){
            initStatements();
        }

        BoundStatement boundGetAllUsers = prepareGetAllUsers.bind();
        ResultSet result = session.execute(boundGetAllUsers);
        List<User> allUsers = new ArrayList<>();
        List<Row> resultRows = result.all();
        resultRows.forEach(x -> allUsers.add(convertResultRowToUser(x)));
        return allUsers;
    }

    public User get(Integer id){
        if (!isInitialized){
            initStatements();
        }

        BoundStatement boundGetUser = prepareGetUser.bind();
        boundGetUser.setInt(0, id);

        ResultSet result = session.execute(boundGetUser);
        return convertResultRowToUser(result.one());
    }

    public User save(User user){
        if (!isInitialized){
            initStatements();
        }

        BoundStatement boundSaveUser = prepareSaveUser.bind();
        boundSaveUser.setInt(0, user.getId());
        boundSaveUser.setString(1, user.getUsername());
        boundSaveUser.setString(2, user.getPassword());
        boundSaveUser.setString(3, user.getEmail());

        ResultSet result = session.execute(boundSaveUser);
        return result.wasApplied() ? get(user.getId()) : null;
    }

    private User convertResultRowToUser(Row row){
        User user = new User();
        user.setId(row.getInt(UserServiceConstants.COLUMN_ID));
        user.setUsername(row.getString(UserServiceConstants.COLUMN_USERNAME));
        user.setPassword(row.getString(UserServiceConstants.COLUMN_PASSWORD));
        user.setEmail(row.getString(UserServiceConstants.COLUMN_EMAIL));

        return user;
    }
}
