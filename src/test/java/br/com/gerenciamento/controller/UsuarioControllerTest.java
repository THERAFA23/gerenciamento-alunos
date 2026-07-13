package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void setup() {
        usuarioRepository.deleteAll();
    }

    
    @Test
    public void testLoginInvalidoNaoRedirecionaParaIndex() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", "usuarioInexistente")
                .param("senha", "senhaErrada"))
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andExpect(MockMvcResultMatchers.view().name("login/cadastro"))
                .andExpect(MockMvcResultMatchers.view().name(not("home/index")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("msg"));
    }

    @Test
    public void testLoginComSucessoRedirecionaParaIndex() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("valido@teste.com");
        usuario.setUser("uservalido");
        usuario.setSenha(Util.md5("senha123")); // O banco armazena a senha hash MD5
        usuarioRepository.save(usuario);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", "uservalido")
                .param("senha", "senha123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home/index"))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("usuarioLogado", org.hamcrest.Matchers.notNullValue()));
    }
    
    @Test
    public void testCadastrarUsuarioComSucesso() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/salvarUsuario")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "novousuario@teste.com")
                .param("user", "novouser")
                .param("senha", "minhasenha"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    public void testLogoutInvalidaSessao() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/logout"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login/login"));
    }
}