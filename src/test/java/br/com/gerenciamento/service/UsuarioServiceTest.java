package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.util.Util;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private ServiceUsuario serviceUsuario;

    
    @Test
    public void testSalvarUsuarioEmailDuplicado() {
        Assert.assertThrows(EmailExistsException.class, () -> {
            Usuario usuario1 = new Usuario();
            usuario1.setEmail("duplicado@teste.com");
            usuario1.setUser("user1");
            usuario1.setSenha("123");
            serviceUsuario.salvarUsuario(usuario1);

            Usuario usuario2 = new Usuario();
            usuario2.setEmail("duplicado@teste.com");
            usuario2.setUser("user2");
            usuario2.setSenha("456");
            serviceUsuario.salvarUsuario(usuario2); // O método deve lançar a exceção aqui
        });
    }

    
    @Test
    public void testSalvarUsuarioCriptografaSenha() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("cripto@teste.com");
        usuario.setUser("criptouser");
        usuario.setSenha("senha123");
        
        serviceUsuario.salvarUsuario(usuario);
        
        Assert.assertNotEquals("senha123", usuario.getSenha());
        Assert.assertEquals(Util.md5("senha123"), usuario.getSenha());
    }

    
    @Test
    public void testLoginUserSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("loginsucesso@teste.com");
        usuario.setUser("loginsucesso");
        usuario.setSenha("12345");
        serviceUsuario.salvarUsuario(usuario);

        // Ao salvar, a senha foi em MD5. Para testar o login de forma limpa como o Service faz:
        Usuario logado = serviceUsuario.loginUser("loginsucesso", Util.md5("12345"));
        Assert.assertNotNull(logado);
    }

    
    @Test
    public void testLoginUserFalha() {
        Usuario logado = serviceUsuario.loginUser("usuariofake", "senhafake");
        Assert.assertNull(logado);
    }
}