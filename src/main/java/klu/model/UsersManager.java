package klu.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import klu.model.*;
import klu.repository.*;
@Service
public class UsersManager {
	
	@Autowired
	UsersRepository UR; 
	
	@Autowired
	EmailManager EM;
	
	@Autowired
	JWTManager JWT;
	public String addUser(Users U) 
	{
		if(UR.validateEmail(U.getEmail()) > 0)
			return "401::Email already exist";
		UR.save(U);
		return "200::User Registered Sucessfully"; 
	}
	public String recoverPassword(String email) 
	{
		Users U = UR.findById(email).get();
		String message = String.format("Dear %s,\n\n Your password is: %s",U.getFullname(),U.getPassword());
		return EM.sendEmail(U.getEmail(), "Food Recipie: Password Recovery", message);
	}
	public String validateCredentials(String email, String password) 
	{
		if(UR.validateCresentials (email, password) > 0)

		{
             String token = JWT.generateToken(email);
             return "200::" + token;
		}
		return "401::Invalid Credentials";
	}
	
	// UPDATE USER
	public String updateUser(String email, Users updatedUser) {
	    if (!UR.existsById(email)) {
	        return "401::User does not exist";
	    }

	    Users existingUser = UR.findById(email).get();
	    existingUser.setFullname(updatedUser.getFullname());
	    existingUser.setPassword(updatedUser.getPassword());

	    UR.save(existingUser);
	    return "200::User updated successfully";
	}

	// DELETE USER
	public String deleteUser(String email) {
	    if (!UR.existsById(email)) {
	        return "401::User does not exist";
	    }

	    UR.deleteById(email);
	    return "200::User deleted successfully";
	}


	

}
