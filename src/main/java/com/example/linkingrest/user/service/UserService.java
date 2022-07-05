package com.example.linkingrest.user.service;

import com.example.linkingrest.exception.EmailLoginFailedCException;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(User user){
        System.out.println(user);
        validationDuplicateEmail(user);
        validationDuplicateName(user);
        userRepository.save(user);
        return user.getId();
    }

    public User login(String email, String password){
        User user = userRepository.findByEmail(email).orElseThrow(EmailLoginFailedCException::new);
        if(!passwordEncoder.matches(password,user.getPassword()))
            throw new EmailLoginFailedCException();
        return user;
    }

    private void validationDuplicateEmail(User user){
        User findUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(findUser != null){
            throw new IllegalStateException("이미 등록된 이메일 입니다.");
        }
    }
    private void validationDuplicateName(User user){
        User findUser = userRepository.findByName(user.getName()).orElse(null);
        if(findUser != null){
            throw new IllegalStateException("이미 등록된 이름 입니다.");
        }
    }

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new IllegalStateException("존재 하지 않는 회원입니다."));
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
