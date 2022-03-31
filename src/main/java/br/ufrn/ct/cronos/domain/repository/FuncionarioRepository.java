package br.ufrn.ct.cronos.domain.repository;


import org.springframework.stereotype.Repository;




@Repository
public interface FuncionarioRepository extends CustomJpaRepository , CustomizedFuncionarioRepository {

	
}
