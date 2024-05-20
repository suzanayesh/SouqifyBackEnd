package com.code.auth.service;

import com.code.auth.dto.user.SimpleUserInfo;
import com.code.auth.dto.user.SupplierResponseDto;
import com.code.auth.dto.user.UserDto;
import com.code.auth.entity.Cart;
import com.code.auth.entity.Role;
import com.code.auth.entity.UserInfo;
import com.code.auth.entity.UserProfile;
import com.code.auth.exception.EmailExistsException;
import com.code.auth.exception.UsernameExistsException;
import com.code.auth.repo.RoleRepository;
import com.code.auth.repo.UserInfoRepository;
import com.code.auth.config.UserInfoDetails;
import com.code.auth.repo.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder encoder;


    public UserInfo getCurrentUserInfo() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal(); // Cast to your UserDetails implementation
        String username = userDetails.getUsername(); // Obtain the username from UserDetails

        return userInfoRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = userInfoRepository.findByName(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }


    public Optional<UserInfo> getUserById(Long id) {
        return userInfoRepository.findById(id);
    }

    //  @Transactional
//  public String addUser(UserDto userDto) {
//    // Check if a user with the given email already exists
//    if (userInfoRepository.findByEmail(userDto.getEmail()).isPresent()) {
//      throw new IllegalStateException("Email already exists: " + userDto.getEmail());
//    }
//
//    UserInfo userInfo = mapper.map(userDto, UserInfo.class);
//    Optional<Role> roleOptional = roleRepository.findById(userDto.getRoleId());
//
//    if (roleOptional.isPresent()) {
//      Role role = roleOptional.get();
//      userInfo.setPassword(encoder.encode(userDto.getPassword()));
//      userInfo.setRole(role);
//      userInfoRepository.save(userInfo);
//      return "User Added Successfully";
//    } else {
//      // Handle case when role with specified ID is not found
//      throw new IllegalArgumentException("Role with ID " + userDto.getRoleId() + " not found");
//    }
//public String addUser(UserDto userDto) {
//        log.info("Checking if user email already exists");
//        if (userInfoRepository.findByEmail(userDto.getEmail()).isPresent()) {
//            log.error("Email already exists: {}", userDto.getEmail());
//            throw new EmailExistsException(userDto.getEmail());
//        }
//
//        log.info("Checking if username already exists");
//        if (userInfoRepository.findByName(userDto.getName()).isPresent()) {
//            log.error("Username already exists: {}", userDto.getName());
//            throw new UsernameExistsException(userDto.getName());
//        }
//
//        UserInfo userInfo = mapper.map(userDto, UserInfo.class);
//        log.info("Looking up role by ID: {}", userDto.getRoleId());
//        Optional<Role> roleOptional = roleRepository.findById(userDto.getRoleId());
//
//        if (roleOptional.isPresent()) {
//            Role role = roleOptional.get();
//            userInfo.setPassword(encoder.encode(userDto.getPassword()));
//            userInfo.setRole(role);
//            userInfoRepository.save(userInfo);
//            log.info("User added successfully");
//            return "User Added Successfully";
//        } else {
//            log.error("Role with ID not found: {}", userDto.getRoleId());
//            throw new IllegalArgumentException("Role with ID " + userDto.getRoleId() + " not found");
//        }
//    }
    @Autowired
    private CartService cartService;
    @Transactional
    public UserInfo addUser(UserDto userDto) {
        log.info("Checking if user email already exists");
        if (userInfoRepository.findByEmail(userDto.getEmail()).isPresent()) {
            log.error("Email already exists: {}", userDto.getEmail());
            throw new EmailExistsException(userDto.getEmail());
        }
        log.info("Checking if username already exists");
        if (userInfoRepository.findByName(userDto.getName()).isPresent()) {
            log.error("Username already exists: {}", userDto.getName());
            throw new UsernameExistsException(userDto.getName());
        }
        Role role = roleRepository.findById(userDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + userDto.getRoleId()));

        UserInfo user = new UserInfo();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setRole(role);
        user.setStorename(userDto.getStorename());

        UserInfo savedUser = userInfoRepository.save(user);
        log.info("User saved successfully with ID: {}", savedUser.getId());

        UserProfile userProfile = new UserProfile();
        userProfile.setProfileId((long) savedUser.getId());
        userProfile.setUser(savedUser);
        userProfile.setEmail(savedUser.getEmail());
        userProfileRepository.save(userProfile);
        log.info("UserProfile created for User ID: {}", savedUser.getId());

        if ("retailer".equalsIgnoreCase(role.getName())) {
            Cart newCart = new Cart();
            newCart.setUser(savedUser);
            cartService.saveCart(newCart);
            log.info("Cart created for retailer with ID: {}", savedUser.getId());
        }

        return savedUser;
    }
//    public List<SupplierResponseDto> getAllSuppliers() {
//        return userInfoRepository.findByRoleId(2).stream()
//                .map(this::convertToSupplierResponseDto)
//                .collect(Collectors.toList());
//    }
public List<SupplierResponseDto> getAllSuppliers() {
    List<UserInfo> suppliers = userInfoRepository.findByRoleId(2); // Assuming role ID 2 is for suppliers
    return suppliers.stream()
            .filter(userInfo -> userInfo.getUserProfile() != null) // Filter out suppliers without UserProfile
            .map(this::convertToSupplierResponseDto)
            .collect(Collectors.toList());
}

    private SupplierResponseDto convertToSupplierResponseDto(UserInfo userInfo) {
        SupplierResponseDto dto = new SupplierResponseDto();
        dto.setUserId((long) userInfo.getId());
        if (userInfo.getUserProfile() != null) {
            dto.setProfileImage(userInfo.getUserProfile().getProfileImage());
            dto.setLocation(userInfo.getUserProfile().getLocation());
        } else {
            dto.setProfileImage(null);
            dto.setLocation(null);
        }
        dto.setRoleId(userInfo.getRole().getId());
        dto.setUsername(userInfo.getName());
        return dto;
    }


    public List<SimpleUserInfo> findUsersByNameContaining(String name) {
        return userInfoRepository.findByNameContaining(name)
                .stream()
                .map(user -> new SimpleUserInfo(
                        user.getName(),
                        user.getStorename(),
                        user.getRole().getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userInfoRepository.existsById(id)) {
            userInfoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Transactional
    public UserInfo updateUser(Long id, UserInfo user) {
        log.info("Attempting to update user with ID: {}", id);
        return userInfoRepository.findById(id)
                .map(existingUser -> {
                    log.info("User found with ID: {}. Updating...", id);
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(encoder.encode(user.getPassword()));
                    existingUser.setStorename(user.getStorename());
                    existingUser.setRole(user.getRole());
                    return userInfoRepository.save(existingUser);
                }).orElseGet(() -> {
                    log.warn("No user found with ID: {}", id);
                    return null;
                });
    }
    public List<UserInfo> getAllUsers() {
        return userInfoRepository.findAll();
    }
    public Optional<UserInfo> getUserByName(String name) {
        return userInfoRepository.findByName(name);
    }


    public Optional<UserInfo> getUserInfoByName(String username) {
        return userInfoRepository.findByName(username);
    }

    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }


}
