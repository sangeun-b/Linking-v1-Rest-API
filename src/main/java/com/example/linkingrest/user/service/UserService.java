package com.example.linkingrest.user.service;

import com.example.linkingrest.error.exception.EmailLoginFailedException;
import com.example.linkingrest.error.exception.EmailSignupFailedException;
import com.example.linkingrest.error.exception.UserNotFoundException;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(User user){
        validationDuplicateEmail(user);
        validationDuplicateName(user);
        userRepository.save(user);
        return user.getId();
    }

    public User login(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(EmailLoginFailedException::new);
        if(!passwordEncoder.matches(password,user.getPassword()))
            throw new EmailLoginFailedException();
        return user;
    }

    private void validationDuplicateEmail(User user){
        User findUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(findUser != null){
            throw new EmailSignupFailedException();
        }
    }
    private void validationDuplicateName(User user){
        User findUser = userRepository.findByName(user.getName()).orElse(null);
        if(findUser != null){
            throw new EmailSignupFailedException();
        }
    }

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
    }

    @Transactional
    public void updateUser(User request, Long id,MultipartFile file){
        validationDuplicateName(request);
        User user = findById(id);
        // 이미지 파일 업로드 후 -> path 넣어주기
        user.updateUser(request.getName(),request.getPassword(),request.getImg());

    }
    @Transactional
    public void deleteUser(Long id){
        userRepository.delete(findById(id));
    }
}
