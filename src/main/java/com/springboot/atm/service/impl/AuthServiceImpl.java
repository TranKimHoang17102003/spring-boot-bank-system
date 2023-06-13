package com.springboot.atm.service.impl;

import com.springboot.atm.entity.Money;
import com.springboot.atm.entity.Role;
import com.springboot.atm.entity.User;
import com.springboot.atm.exception.AtmAPIException;
import com.springboot.atm.payload.dto.UserDto;
import com.springboot.atm.payload.dto.UserListResponse;
import com.springboot.atm.payload.form.LoginForm;
import com.springboot.atm.payload.form.RegisterForm;
import com.springboot.atm.repository.RoleRepository;
import com.springboot.atm.repository.UserRepository;
import com.springboot.atm.security.JwtTokenProvider;
import com.springboot.atm.service.AuthService;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper mapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, ModelMapper mapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mapper = mapper;
    }

    @Override
    public String login(LoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.getUsername(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public String register(RegisterForm registerForm) {

        if(!registerForm.getPassword().equals(registerForm.getCheckPass())) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Password not equal!.");
        }

        if(!registerForm.getPin().equals(registerForm.getCheckPin())) {
            throw new AtmAPIException(HttpStatus.BAD_REQUEST, "Pin not equal!.");
        }

        if(!userRepository.existsByUsername("admin")) {
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName("ROLE_ADMIN");
            roles.add(role);

            User user = new User();
            LocalDate localDate = LocalDate.of(2003, 10, 17);

            user.setBank("HKTBank");
            user.setEmail("admin@gmail.com");
            user.setBirthday(localDate);
            user.setAccountNumber("00246000000");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setPhoneNumber("0000000000");
            user.setPin("000000");
            user.setPassword("$2a$10$S5bv2eBhHJqpjLJR.fqq2OPEg/3kT57skektK8be1Q0LtdYpEKrn2");
            user.setUsername("admin");

            Money money = new Money();
            money.setCurrentAmount((double) 0);

            user.setRoles(roles);
            user.setMoney(money);
            money.setUser(user);

            userRepository.save(user);
        }

        if(!userRepository.existsByUsername("hoang123")) {
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName("ROLE_USER");
            roles.add(role);

            User user = new User();
            LocalDate localDate = LocalDate.of(2003, 10, 17);

            user.setBank("HKTBank");
            user.setEmail("hoang@gmail.com");
            user.setBirthday(localDate);
            user.setAccountNumber("002460111111111");
            user.setFirstName("Hoang");
            user.setLastName("Tran Kim");
            user.setPhoneNumber("0111111111");
            user.setPin("171003");
            user.setPassword("$2a$10$EL9sr97Gjedq.Qqtuv1hHuT6FnAwdVmGYIfMLF2gLnvsdM/wLoofi");
            user.setUsername("hoang123");

            Money money = new Money();
            money.setCurrentAmount((double) 0);

            user.setRoles(roles);
            user.setMoney(money);
            money.setUser(user);

            userRepository.save(user);
        }

        if(!userRepository.existsByUsername("cuong123")) {
            Set<Role> roles = new HashSet<>();
            Role role = roleRepository.findByName("ROLE_USER");
            roles.add(role);

            User user = new User();
            LocalDate localDate = LocalDate.of(2003, 8, 18);

            user.setBank("HKTBank");
            user.setEmail("cuong@gmail.com");
            user.setBirthday(localDate);
            user.setAccountNumber("002460123456789");
            user.setFirstName("Cuong");
            user.setLastName("Le Trung");
            user.setPhoneNumber("0123456789");
            user.setPin("180803");
            user.setPassword("$2a$10$JY3m5YxkqsVGPxPcbKihwuPUMe5hzq6FhpSxi7j.ttOv802TfzMCC");
            user.setUsername("cuong123");

            Money money = new Money();
            money.setCurrentAmount((double) 0);

            user.setRoles(roles);
            user.setMoney(money);
            money.setUser(user);

            userRepository.save(user);
        }

        User user = new User();

        user.setBank("HKTBank");
        user.setEmail(registerForm.getEmail());
        user.setBirthday(registerForm.getBirthday());
        user.setAccountNumber("00246"+registerForm.getPhone());
        user.setFirstName(registerForm.getFirstName());
        user.setLastName(registerForm.getLastName());
        user.setPhoneNumber(registerForm.getPhone());
        user.setPin(registerForm.getPin());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setUsername(registerForm.getUsername());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");

        Money money = new Money();
        money.setCurrentAmount((double) 0);

        roles.add(userRole);
        user.setRoles(roles);
        user.setMoney(money);
        money.setUser(user);

        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public UserDto getInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new AtmAPIException(HttpStatus.BAD_REQUEST,"User not found with id: " + id));
        return mapToDto(user);
    }

    @Override
    public UserListResponse getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equals(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<User> listUserPage = userRepository.findAll(pageable);

        List<User> listUser = listUserPage.getContent();

        List<UserDto> content = listUser.stream().map(user -> mapToDto(user)).collect(Collectors.toList());

        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setContent(content);
        userListResponse.setPageNo(listUserPage.getNumber());
        userListResponse.setPageSize(listUserPage.getSize());
        userListResponse.setTotalElements(listUserPage.getTotalElements());
        userListResponse.setTotalPages(listUserPage.getTotalPages());
        userListResponse.setLast(listUserPage.isLast());

        return userListResponse;
    }

    private UserDto mapToDto(User user) {
        UserDto userDto = mapper.map(user, UserDto.class);
        Set<Role> roles = user.getRoles();
        Set<String> roles1 = new HashSet<>();
        for (Role role:
             roles) {
            if (role.getName().startsWith("ROLE_")) {
                roles1.add(role.getName());
            }
        }
        userDto.setRoles(roles1);
        return userDto;
    }
}
