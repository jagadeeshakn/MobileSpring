package com.mobile.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.mobile.store.model.User;
import com.mobile.store.repo.UserRepository;
import com.mobile.store.service.SecretKeyGenerator;
import com.mobile.store.util.EncodeDecodeUtil;

@Service
public class SecretKeyGeneratorImpl implements SecretKeyGenerator {

	@Autowired
	private EncodeDecodeUtil encodeDecodeUtil; 
	
    @Autowired
    private UserRepository userRepository;
    
	@Override
	public String genereateUserSecretkey(String userName) {
		User user = userRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("User not found for this userData :: "+userName));
		 return encodeDecodeUtil.encodeString(user.getUsername());
	}

}
