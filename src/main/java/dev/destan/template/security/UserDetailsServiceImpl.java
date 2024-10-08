package dev.destan.template.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginUserRepository loginUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found."));
    }
}
