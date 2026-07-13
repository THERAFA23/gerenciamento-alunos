package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.repository.AlunoRepository;
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

import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void setup() {
        alunoRepository.deleteAll();
    }

    @Test
    public void testPesquisarAlunoNomeVazioRetornaListaCompleta() throws Exception {
        
        Aluno aluno1 = new Aluno();

        aluno1.setNome("Mauricio Torres");
        aluno1.setMatricula("111222");
        aluno1.setCurso(Curso.ADMINISTRACAO);
        aluno1.setStatus(Status.ATIVO);
        aluno1.setTurno(Turno.MATUTINO);
        alunoRepository.save(aluno1);

        Aluno aluno2 = new Aluno();

        aluno2.setNome("Beatriz Cavalcanti");
        aluno2.setMatricula("333444");
        aluno2.setCurso(Curso.INFORMATICA);
        aluno2.setStatus(Status.ATIVO);
        aluno2.setTurno(Turno.NOTURNO);
        alunoRepository.save(aluno2);


        mockMvc.perform(MockMvcRequestBuilders.post("/pesquisar-aluno")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nome", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("Aluno/pesquisa-resultado"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ListaDeAlunos"))
                // Garante que ambos os alunos cadastrados foram retornados na lista completa
                .andExpect(MockMvcResultMatchers.model().attribute("ListaDeAlunos", hasSize(2)));
    }

    @Test
    public void testInserirAlunoComSucesso() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/InsertAlunos")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nome", "Adriano Lima")
                .param("matricula", "123457")
                .param("curso", Curso.INFORMATICA.name())
                .param("status", Status.ATIVO.name())
                .param("turno", Turno.MATUTINO.name()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/alunos-adicionados"));
    }

    
    @Test
    public void testInserirAlunoComErroValidacao() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/InsertAlunos")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nome", "Igo") 
                .param("matricula", "123")
                .param("curso", Curso.ADMINISTRACAO.name())
                .param("status", Status.ATIVO.name())
                .param("turno", Turno.NOTURNO.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("Aluno/formAluno"))
                .andExpect(MockMvcResultMatchers.model().hasErrors());
    }

    @Test
    public void testRemoverAlunoExistente() throws Exception {
        Aluno aluno = new Aluno();

        aluno.setNome("Aluno Exclusao");
        aluno.setMatricula("99999");
        aluno.setCurso(Curso.DIREITO);
        aluno.setStatus(Status.ATIVO);
        aluno.setTurno(Turno.NOTURNO);
        alunoRepository.save(aluno);

        mockMvc.perform(MockMvcRequestBuilders.get("/remover/" + aluno.getId()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/alunos-adicionados"));
    }
}