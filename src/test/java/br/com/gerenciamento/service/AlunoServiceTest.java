package br.com.gerenciamento.service;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    
    @Test
    public void getById() {
        Aluno aluno = new Aluno();
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);

        // Ajustado para usar o ID gerado pelo banco ao invés de fixar 1L
        Aluno alunoRetorno = this.serviceAluno.getById(aluno.getId());
        Assert.assertTrue(alunoRetorno.getNome().equals("Vinicius"));
    }

    @Test
    public void salvarSemNome() {
        Aluno aluno = new Aluno();
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        Assert.assertThrows(ConstraintViolationException.class, () -> {
            this.serviceAluno.save(aluno);
        });
    }

    
    @Test
    public void testFindByNomeContainingIgnoreCase() {
        Aluno aluno = new Aluno();
        aluno.setNome("Rafael Souza");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.INFORMATICA);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("654321");
        this.serviceAluno.save(aluno);

        List<Aluno> resultado = this.serviceAluno.findByNomeContainingIgnoreCase("rafael");
        Assert.assertFalse(resultado.isEmpty());
        Assert.assertTrue(resultado.stream().anyMatch(a -> a.getNome().equals("Rafael Souza")));
    }

    @Test
    public void testFindAll() {
        Aluno aluno = new Aluno();
        aluno.setNome("Teste Todos");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ENFERMAGEM);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("999888");
        this.serviceAluno.save(aluno);

        List<Aluno> todos = this.serviceAluno.findAll();
        Assert.assertFalse(todos.isEmpty());
        Assert.assertTrue(todos.size() >= 1);
    }
}