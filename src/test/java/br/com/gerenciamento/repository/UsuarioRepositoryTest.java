package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Usuario;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    @Test
    public void testBuscarLogin() {
        Usuario usuario = new Usuario();
        usuario.setEmail("login@teste.com");
        usuario.setUser("userlogin");
        usuario.setSenha("senha123");
        usuarioRepository.save(usuario);

        Usuario logado = usuarioRepository.buscarLogin("userlogin", "senha123");
        Assert.assertNotNull(logado);
        Assert.assertEquals("login@teste.com", logado.getEmail());
    }

    
    @Test
    public void testFindByEmail() {
        Usuario usuario = new Usuario();
        usuario.setEmail("email@teste.com");
        usuario.setUser("useremail");
        usuario.setSenha("12345");
        usuarioRepository.save(usuario);

        Usuario resultado = usuarioRepository.findByEmail("email@teste.com");
        Assert.assertNotNull(resultado);
        Assert.assertEquals("useremail", resultado.getUser());
    }

    
    @Test
    public void testSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("novo@teste.com");
        usuario.setUser("novouser");
        usuario.setSenha("abcde");
        
        Usuario salvo = usuarioRepository.save(usuario);
        Assert.assertNotNull(salvo.getId());
    }

    
    @Test
    public void testBuscarLoginFalha() {
        Usuario logado = usuarioRepository.buscarLogin("inexistente", "errada");
        Assert.assertNull(logado);
    }
}