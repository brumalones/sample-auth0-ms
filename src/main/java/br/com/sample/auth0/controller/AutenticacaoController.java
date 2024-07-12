package br.com.sample.auth0.controller;

import br.com.sample.auth0.domain.usuario.DadosAutenticacao;
import br.com.sample.auth0.domain.usuario.SignUp;
import br.com.sample.auth0.domain.usuario.Usuario;
import br.com.sample.auth0.domain.usuario.UsuarioRepository;
import br.com.sample.auth0.infra.security.DadosTokenJWT;
import br.com.sample.auth0.infra.security.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/security")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/SingIn")
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }


    @PostMapping("/SingUp")
    @Transactional
    public ResponseEntity<Usuario> Registrar(@RequestBody @Valid SignUp signUp, UriComponentsBuilder componentsBuilder) {
        if (!usuarioRepository.existsByLogin(signUp.login())) {
            var usuario = new Usuario(signUp);
            usuarioRepository.save(usuario);
            var uri = componentsBuilder.path("security/login/{id}").buildAndExpand(usuario.getId()).toUri();
            return ResponseEntity.created(uri).body(usuario);

        }
        throw new RuntimeException("Usuário já existe");

    }

}
