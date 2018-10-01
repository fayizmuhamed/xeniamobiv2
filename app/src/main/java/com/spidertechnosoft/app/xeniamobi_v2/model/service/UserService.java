package com.spidertechnosoft.app.xeniamobi_v2.model.service;

import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserService implements IEntityService<User> {

    @Override
    public Long save(User user) {

        // check if the user is null
        if ( user == null ) return null;

        User existingUser=findByUid(user.getUid());

        if(existingUser==null){

            return user.save();

        }else {

            user.setId(existingUser.getId());

            return user.save();
        }

    }

    @Override
    public User findById(Long id) {

        // Create the User object
        User user = new User();

        // the user by id
        return  user.findById(User.class, id);
    }

    @Override
    public List<User> findByQuery(String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {

        // Create the User object
        User user = new User();

        // Return the list
        return user.find(User.class, whereClause, whereArgs, groupBy, orderBy, limit);
    }

    @Override
    public boolean delete(Long id) {

        // Create the User object
        User user = new User();

        // Return the response after delete
        return user.delete();
    }

    @Override
    public boolean delete(User user) {

        // Return the response after delete
        return user.delete();
    }

    public User findByUid(String uid) {

        // Create the Category object
        List<User> users = new ArrayList<>();

        // the user by username
        users=  User.find(User.class, "uid = ?", uid);

        return (users!=null&&users.size()>0)?users.get(0):null;

    }

    public User findByUsername(String username) {

        // Create the User object
        List<User> users = new ArrayList<>();

        User user=null;

        // the user by username
        users=  user.find(User.class, "username = ?", username);

        return users.size()>0?users.get(0):null;

    }

    public  List<User> findActiveUsers() {


        // the user by username
        return User.find(User.class, "active = ?",new String[]{"1"},null, "name ASC",null);


    }

    public  List<User> findActiveStaffUsers(User user) {


        // the user by username
        return User.find(User.class, "active = ? and (type=? or uid=?)",new String[]{"1", String.valueOf(UserType.STAFF),user.getUid()},null, "name ASC",null);


    }
}
