package com.springboot.atm.service.impl;

import com.springboot.atm.entity.Notify;
import com.springboot.atm.entity.User;
import com.springboot.atm.payload.dto.NotifyDto;
import com.springboot.atm.repository.NotifyRepository;
import com.springboot.atm.repository.UserRepository;
import com.springboot.atm.service.NotifyService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotifyServiceImpl implements NotifyService {
    private final NotifyRepository notifyRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public NotifyServiceImpl(NotifyRepository notifyRepository, UserRepository userRepository, ModelMapper mapper) {
        this.notifyRepository = notifyRepository;
        this.userRepository = userRepository;
        this.mapper=mapper;
    }

    @Override
    public List<NotifyDto> getMyNotify() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number " + name));

        return notifyRepository.findAllByUser(user).stream().map(notify -> mapToDto(notify)).collect(Collectors.toList());
    }

    @Override
    public List<NotifyDto> getAll() {
        return notifyRepository.findAll().stream().map(notify -> mapToDto(notify)).collect(Collectors.toList());
    }

    @Override
    public List<NotifyDto> getByUserId(Long userId) {
        return notifyRepository.findAllByUserId(userId).stream().map(notify -> mapToDto(notify)).collect(Collectors.toList());
    }

    private NotifyDto mapToDto(Notify notify) {
        return mapper.map(notify, NotifyDto.class);
    }
}
