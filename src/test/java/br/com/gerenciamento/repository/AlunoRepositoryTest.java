package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoRepositoryTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    public void testFindByStatusInativo() {
        Aluno alunoAtivo = new Aluno();
        alunoAtivo.setNome("Carlos Ativo");
        alunoAtivo.setTurno(Turno.MATUTINO);
        alunoAtivo.setCurso(Curso.INFORMATICA);
        alunoAtivo.setStatus(Status.ATIVO);
        alunoAtivo.setMatricula("111111");
        alunoRepository.save(alunoAtivo);

        Aluno alunoInativo = new Aluno();
        alunoInativo.setNome("Marcos Inativo");
        alunoInativo.setTurno(Turno.NOTURNO);
        alunoInativo.setCurso(Curso.DIREITO);
        alunoInativo.setStatus(Status.INATIVO);
        alunoInativo.setMatricula("222222");
        alunoRepository.save(alunoInativo);

        List<Aluno> inativos = alunoRepository.findByStatusInativo();
        
        // Verifica se apenas o inativo foi retornado
        boolean contemApenasInativos = inativos.stream().allMatch(a -> a.getStatus() == Status.INATIVO);
        Assert.assertTrue(contemApenasInativos);
        Assert.assertTrue(inativos.stream().anyMatch(a -> a.getNome().equals("Marcos Inativo")));
    }

    
    @Test
    public void testFindByStatusAtivo() {
        Aluno aluno = new Aluno();
        aluno.setNome("Ana Ativa");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("333333");
        alunoRepository.save(aluno);

        List<Aluno> ativos = alunoRepository.findByStatusAtivo();
        Assert.assertFalse(ativos.isEmpty());
    }

    
    @Test
    public void testSalvarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("Pedro Salvo");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.BIOMEDICINA);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("444444");
        
        Aluno salvo = alunoRepository.save(aluno);
        Assert.assertNotNull(salvo.getId());
    }

    
    @Test
    public void testDeletarAluno() {
        Aluno aluno = new Aluno();
        aluno.setNome("Aluno Deletado");
        aluno.setTurno(Turno.MATUTINO);
        aluno.setCurso(Curso.CONTABILIDADE);
        aluno.setStatus(Status.INATIVO);
        aluno.setMatricula("555555");
        Aluno salvo = alunoRepository.save(aluno);
        
        alunoRepository.deleteById(salvo.getId());
        Assert.assertFalse(alunoRepository.findById(salvo.getId()).isPresent());
    }
}