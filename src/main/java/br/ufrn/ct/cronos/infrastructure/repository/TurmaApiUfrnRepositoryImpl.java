package br.ufrn.ct.cronos.infrastructure.repository;

import br.ufrn.ct.cronos.domain.conversor.Conversor;
import br.ufrn.ct.cronos.domain.filter.ConsultaUnidadeAPIUFRNFilter;
import br.ufrn.ct.cronos.domain.model.dto.DepartamentoDTO;
import br.ufrn.ct.cronos.domain.model.dto.TurmaDTO;
import br.ufrn.ct.cronos.domain.repository.TurmaApiUfrnRepository;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
public class TurmaApiUfrnRepositoryImpl extends ApiUfrnAbstract implements TurmaApiUfrnRepository {

    @Override
    public List<DepartamentoDTO> retornaUnidadesAcademicasPorIdCentro(Integer idCentro) {
        /*
			 * ### Tipos de Unidade:
			 * [{"id-tipo-unidade":1,"descricao":"Departamento"},
			 * {"id-tipo-unidade":2,"descricao":"Escola"},
			 * {"id-tipo-unidade":3,"descricao":"Programa de Pós Graduação"},
			 * {"id-tipo-unidade":4,"descricao":"Centro"},
			 * {"id-tipo-unidade":5,"descricao":"Unidade Acadêmica Específica"},
			 * {"id-tipo-unidade":6,"descricao":"Coordenação de Curso"},
			 * {"id-tipo-unidade":7,"descricao":"Pró-Reitoria"},
			 * {"id-tipo-unidade":8,"descricao":"Coordencação de Curso Lato"},
			 * {"id-tipo-unidade":9,"descricao":"Residência Médica"},
			 * {"id-tipo-unidade":10,"descricao":"Coordenação de Pós-Graduação"}]
			 * */
        ResponseEntity<String> resposta = getRespostaJSONPaginado(getUrlBaseSistemas() + "unidade/"+ getVersao() +"/unidades?id-unidade-responsavel="+idCentro+"&id-tipo-unidade=1,2,3,5,6,8,10");
        HttpHeaders httpHeadersTurmas = resposta.getHeaders();
        // Obtendo o total de departamentos
        //Construtor Double(String) está depreciado desde a versão 9 do Java
        //Double doubleTotalTurmas = new Double(httpHeadersTurmas.getFirst("x-total"));
        Double doubleTotalDepartamentos = Double.parseDouble(httpHeadersTurmas.getFirst("x-total"));
        // Calculando a quantidade de páginas e de repetições no laço(for) seguinte
        Double doubleLacos = doubleTotalDepartamentos/100;
        int inteiroLacos = doubleLacos.intValue();
        inteiroLacos++;
        /*
           * A variável offSet define a posição inicial da paginação
           * Como a paginação é feita de 100 em 100 registros, então o offSet começa em 0, depois 100 (se tiver mais uma página), depois 200 (se tiver mais outra página), e assim por diante
        */
        int offSetDepartamentos = 0;
             
             List<DepartamentoDTO> retorno = new ArrayList<DepartamentoDTO>(0);
             
             for (int i = 0; i < inteiroLacos; i++) {
                 resposta = getRespostaJSON(getUrlBaseSistemas() + "unidade/"+ getVersao() +"/unidades?id-unidade-responsavel="+idCentro+"&id-tipo-unidade=1,2,3,5,6,8,10&limit=100&offset="+offSetDepartamentos);
                 JSONArray jsonArrayDepartamentos = new JSONArray(resposta.getBody());
                 Iterator<Object> iteratorArrayDepartamentos = jsonArrayDepartamentos.iterator();
                 while (iteratorArrayDepartamentos.hasNext()) {
                     JSONObject jsonObjectDepartamento = (JSONObject) iteratorArrayDepartamentos.next();
                     DepartamentoDTO departamentoDTO = new DepartamentoDTO();
                     departamentoDTO.setCodigoUnidade((Integer) jsonObjectDepartamento.get("codigo-unidade"));
                     try {
                         departamentoDTO.setEmail((String) jsonObjectDepartamento.get("email"));
                     } catch (Exception e) {
                         departamentoDTO.setEmail(new String(""));
                     }
                     departamentoDTO.setHierarquiaOrganizacional((String) jsonObjectDepartamento.get("hierarquia-organizacional"));
                     try {
                         departamentoDTO.setIdAmbienteOrganizacional((Integer) jsonObjectDepartamento.get("id-ambiente-organizacional"));
                     } catch (Exception e) {
                         //O construtor Integer(int) está depreciado desde a versão 9 do Java
                         //departamentoDTO.setIdAmbienteOrganizacional(new Integer(0));
                         departamentoDTO.setIdAmbienteOrganizacional(0);
                     }
                     
                     try {
                         departamentoDTO.setIdAreaAtuacaoUnidade((Integer) jsonObjectDepartamento.get("id-area-atuacao-unidade"));
                     } catch (Exception e) {
                         //O construtor Integer(int) está depreciado desde a versão 9 do Java
                         //departamentoDTO.setIdAreaAtuacaoUnidade(new Integer(0));
                         departamentoDTO.setIdAreaAtuacaoUnidade(0);
                     }
                     try {
                         departamentoDTO.setIdClassificacaoUnidade((Integer) jsonObjectDepartamento.get("id-classificacao-unidade"));
                     } catch (Exception e) {
                         //O construtor Integer(int) está depreciado desde a versão 9 do Java
                         //departamentoDTO.setIdClassificacaoUnidade(new Integer(0));
                         departamentoDTO.setIdClassificacaoUnidade(0);
                     }
                     try {
                         departamentoDTO.setIdMunicipio((Integer) jsonObjectDepartamento.get("id-municipio"));
                     } catch (Exception e) {
                         //O construtor Integer(int) está depreciado desde a versão 9 do Java
                         //departamentoDTO.setIdMunicipio(new Integer(0));
                         departamentoDTO.setIdMunicipio(0);
                     }
                     //departamentoDTO.setIdNivelOrganizacional((Integer) jsonObjectDepartamento.get("id-nivel-organizacional"));
                     //departamentoDTO.setIdTipoUnidadeOrganizacional((Integer) jsonObjectDepartamento.get("id-tipo-unidade-organizacional"));
                     departamentoDTO.setIdUnidade((Integer) jsonObjectDepartamento.get("id-unidade"));
                     departamentoDTO.setIdUnidadeGestora((Integer) jsonObjectDepartamento.get("id-unidade-gestora"));
                     departamentoDTO.setNomeUnidade((String) jsonObjectDepartamento.get("nome-unidade"));
                     departamentoDTO.setSigla((String) jsonObjectDepartamento.get("sigla"));
                     //departamentoDTO.setTelefones((String) jsonObjectDepartamento.get("telefones"));
                     departamentoDTO.setUnidadePatrimonial((Boolean) jsonObjectDepartamento.get("unidade-patrimonial"));
                     retorno.add(departamentoDTO);
                 }
                 offSetDepartamentos = offSetDepartamentos + 100;
             }
             
            return retorno;
    }

    @Override
    public List<DepartamentoDTO> retornaUnidadesPorNomeCentro(ConsultaUnidadeAPIUFRNFilter filtro) {
            var urlRequestPaginado = new StringBuilder(getUrlBaseSistemas() + "unidade/"+ getVersao() +"/unidades?");
            if (StringUtils.hasText(filtro.getNome())) {
                urlRequestPaginado.append("nome-unidade="+filtro.getNome()+"&");
            }
            if (filtro.getIdUnidadeResponsavel() != null) {
                urlRequestPaginado.append("id-unidade-responsavel="+filtro.getIdUnidadeResponsavel()+"&");
            }
            if(filtro.getApenasUnidadesAcademicas()){
                urlRequestPaginado.append("id-tipo-unidade=1,2,3,5,6,8,10");
            } else {
                urlRequestPaginado.append("id-tipo-unidade=1,2,3,4,5,6,7,8,9,10");
            }
            log.info("URL de Consulta de Unidades a API UFRN (Passo 1): " + urlRequestPaginado.toString());
		   	ResponseEntity<String> resposta = getRespostaJSONPaginado(urlRequestPaginado.toString());
			HttpHeaders httpHeadersTurmas = resposta.getHeaders();
			// Obtendo o total de departamentos
            //Construtor Double(String) está depreciado desde a versão 9 do Java
			//Double doubleTotalTurmas = new Double(httpHeadersTurmas.getFirst("x-total"));
            Double doubleTotalDepartamentos = Double.parseDouble(httpHeadersTurmas.getFirst("x-total"));
			// Calculando a quantidade de páginas e de repetições no laço(for) seguinte
			Double doubleLacos = doubleTotalDepartamentos/100;
			int inteiroLacos = doubleLacos.intValue();
			inteiroLacos++;
			/*
			 * A variável offSet define a posição inicial da paginação
			 * Como a paginação é feita de 100 em 100 registros, então o offSet começa em 0, depois 100 (se tiver mais uma página), depois 200 (se tiver mais outra página), e assim por diante
			 */
			int offSetDepartamentos = 0;
			
			List<DepartamentoDTO> retorno = new ArrayList<DepartamentoDTO>(0);
			var urlRequest = new StringBuilder(urlRequestPaginado.toString());
            log.info("URL de Consulta de Unidades a API UFRN (Passo 2): " + urlRequest.toString());
			for (int i = 0; i < inteiroLacos; i++) {
                urlRequest.append("&limit=100&offset="+offSetDepartamentos);
				resposta = getRespostaJSON(urlRequest.toString());
				JSONArray jsonArrayDepartamentos = new JSONArray(resposta.getBody());
				Iterator<Object> iteratorArrayDepartamentos = jsonArrayDepartamentos.iterator();
				while (iteratorArrayDepartamentos.hasNext()) {
					JSONObject jsonObjectDepartamento = (JSONObject) iteratorArrayDepartamentos.next();
					DepartamentoDTO departamentoDTO = new DepartamentoDTO();
					departamentoDTO.setCodigoUnidade((Integer) jsonObjectDepartamento.get("codigo-unidade"));
					try {
						departamentoDTO.setEmail((String) jsonObjectDepartamento.get("email"));
					} catch (Exception e) {
						departamentoDTO.setEmail(new String(""));
					}
					departamentoDTO.setHierarquiaOrganizacional((String) jsonObjectDepartamento.get("hierarquia-organizacional"));
					try {
						departamentoDTO.setIdAmbienteOrganizacional((Integer) jsonObjectDepartamento.get("id-ambiente-organizacional"));
					} catch (Exception e) {
                        //O construtor Integer(int) está depreciado desde a versão 9 do Java
						//departamentoDTO.setIdAmbienteOrganizacional(new Integer(0));
                        departamentoDTO.setIdAmbienteOrganizacional(0);
					}
					
					try {
						departamentoDTO.setIdAreaAtuacaoUnidade((Integer) jsonObjectDepartamento.get("id-area-atuacao-unidade"));
					} catch (Exception e) {
                        //O construtor Integer(int) está depreciado desde a versão 9 do Java
						//departamentoDTO.setIdAreaAtuacaoUnidade(new Integer(0));
                        departamentoDTO.setIdAreaAtuacaoUnidade(0);
					}
					try {
						departamentoDTO.setIdClassificacaoUnidade((Integer) jsonObjectDepartamento.get("id-classificacao-unidade"));
					} catch (Exception e) {
                        //O construtor Integer(int) está depreciado desde a versão 9 do Java
						//departamentoDTO.setIdClassificacaoUnidade(new Integer(0));
                        departamentoDTO.setIdClassificacaoUnidade(0);
					}
					try {
						departamentoDTO.setIdMunicipio((Integer) jsonObjectDepartamento.get("id-municipio"));
					} catch (Exception e) {
                        //O construtor Integer(int) está depreciado desde a versão 9 do Java
						//departamentoDTO.setIdMunicipio(new Integer(0));
                        departamentoDTO.setIdMunicipio(0);
					}
					//departamentoDTO.setIdNivelOrganizacional((Integer) jsonObjectDepartamento.get("id-nivel-organizacional"));
					//departamentoDTO.setIdTipoUnidadeOrganizacional((Integer) jsonObjectDepartamento.get("id-tipo-unidade-organizacional"));
					departamentoDTO.setIdUnidade((Integer) jsonObjectDepartamento.get("id-unidade"));
					departamentoDTO.setIdUnidadeGestora((Integer) jsonObjectDepartamento.get("id-unidade-gestora"));
					departamentoDTO.setNomeUnidade((String) jsonObjectDepartamento.get("nome-unidade"));
					departamentoDTO.setSigla((String) jsonObjectDepartamento.get("sigla"));
					//departamentoDTO.setTelefones((String) jsonObjectDepartamento.get("telefones"));
					departamentoDTO.setUnidadePatrimonial((Boolean) jsonObjectDepartamento.get("unidade-patrimonial"));
					retorno.add(departamentoDTO);
				}
				offSetDepartamentos = offSetDepartamentos + 100;
			}
			
		   return retorno;
    }

    @Override
    public List<TurmaDTO> retornaTurmasAbertasPorSiglaNivelEnsinoIdDepartamentoAnoPeriodo(Set<String> siglasnivelEnsino, Integer idDepartamento, Integer ano, Integer periodo) {
        long inicioExecucao = System.currentTimeMillis();
        String stringSiglaNivelEnsino = "";

        String[] arraySiglasNivelEnsino = siglasnivelEnsino.toArray(new String[siglasnivelEnsino.size()]);
        for (int i = 0; i < arraySiglasNivelEnsino.length; i++) {
            if (i == arraySiglasNivelEnsino.length - 1) {
                stringSiglaNivelEnsino = stringSiglaNivelEnsino + arraySiglasNivelEnsino[i];
            } else {
                stringSiglaNivelEnsino = stringSiglaNivelEnsino + arraySiglasNivelEnsino[i] + ",";
            }
        }
        /*
			 * ### Situações de Turma: 
			 * [{"id-situacao-turma":1,"descricao":"ABERTA"},
			 * {"id-situacao-turma":2,"descricao":"A DEFINIR DOCENTE"},
			 * {"id-situacao-turma":3,"descricao":"CONSOLIDADA"},
			 * {"id-situacao-turma":4,"descricao":"EXCLUÍDA"},
			 * {"id-situacao-turma":6,"descricao":"INTERROMPIDA"},
			 * {"id-situacao-turma":7,"descricao":"AGUARDANDO HOMOLOGAÇÃO"}]
		* */
        List<TurmaDTO> turmasSIGAA = new ArrayList<>();
        ResponseEntity<String> resposta = getRespostaJSONPaginado(getUrlBaseSistemas() +"turma/"+ getVersao() +"/turmas?"
                    +"sigla-nivel="+stringSiglaNivelEnsino
                   +"&id-unidade="+idDepartamento+"&ano="+ano+"&periodo="+periodo+"&id-situacao-turma=1,2");
        HttpHeaders httpHeadersTurmas = resposta.getHeaders();
        Double doubleTotalTurmas = Double.parseDouble(httpHeadersTurmas.getFirst("x-total"));
        Double doubleLacos = doubleTotalTurmas/100;
        int inteiroLacos = doubleLacos.intValue();
        inteiroLacos++;
        int offSetTurmas = 0;
        for (int i = 0; i < inteiroLacos; i++) {
            resposta = getRespostaJSON(getUrlBaseSistemas() + "turma/"+ getVersao() +"/turmas?"
                    +"sigla-nivel="+stringSiglaNivelEnsino
                    +"&id-unidade="+idDepartamento+"&ano="+ano+"&periodo="+periodo+"&id-situacao-turma=1,2&limit=100&offset="+offSetTurmas);
            JSONArray jsonArrayTurmas = new JSONArray(resposta.getBody());
            Iterator<Object> iteratorArrayTurmas = jsonArrayTurmas.iterator();
            while (iteratorArrayTurmas.hasNext()) {
                JSONObject jsonObjectTurma = (JSONObject) iteratorArrayTurmas.next();
                Integer idTurma = (Integer) jsonObjectTurma.get("id-turma");
                        
                TurmaDTO turmaDTO = Conversor.convertJsonObjectToTurmaDTO(jsonObjectTurma);
                turmaDTO.setIdUnidade(idDepartamento);
        
                ResponseEntity<String> respostaQtdDiscentes = getRespostaJSONPaginado(getUrlBaseSistemas() + "turma/" + getVersao() + "/participantes?id-turma="+idTurma+"&id-tipo-participante=4");
                HttpHeaders httpHeaders = respostaQtdDiscentes.getHeaders();
                                
                Double doubleTotalDiscentes = Double.parseDouble(httpHeaders.getFirst("x-total"));
                turmaDTO.setQtdMatriculados(doubleTotalDiscentes.intValue());
                resposta = getRespostaJSON(getUrlBaseSistemas() + "turma/" + getVersao() + "/participantes?id-turma="+idTurma+"&id-tipo-participante=1");
                JSONArray jsonArrayDocentes = new JSONArray(resposta.getBody());
                Iterator<Object> iteratorArrayDocentes = jsonArrayDocentes.iterator();
                while (iteratorArrayDocentes.hasNext()) {
                    JSONObject jsonObjectDocente = (JSONObject) iteratorArrayDocentes.next();
                    turmaDTO.getDocentesList().add(Conversor.convertJsonObjectToDocenteDTO(jsonObjectDocente));
                }			
                turmasSIGAA.add(turmaDTO);
            }
            offSetTurmas = offSetTurmas + 100;
        }
        long fimExecucao = System.currentTimeMillis();
        System.out.println("#### Método retornaTurmasAbertasPorSiglaNivelEnsinoIdDepartamentoAnoPeriodo(Id do Dep: "+idDepartamento+") - Tempo execução(ms): " + String.valueOf(fimExecucao - inicioExecucao));
        return turmasSIGAA;
    }

    @Override
    public DepartamentoDTO retornaUnidadePorId(Integer idUnidade) {
        List<DepartamentoDTO> retorno = new ArrayList<DepartamentoDTO>(0);
		ResponseEntity<String> resposta = getRespostaJSONPaginado(getUrlBaseSistemas() + "unidade/"+ getVersao() +"/unidades/"+idUnidade);
        JSONObject jsonObjectDepartamento = new JSONObject(resposta.getBody());
        DepartamentoDTO departamentoDTO = new DepartamentoDTO();
        departamentoDTO.setCodigoUnidade((Integer) jsonObjectDepartamento.get("codigo-unidade"));
        try {
            departamentoDTO.setEmail((String) jsonObjectDepartamento.get("email"));
        } catch (Exception e) {
            departamentoDTO.setEmail(new String(""));
        }
        departamentoDTO.setHierarquiaOrganizacional((String) jsonObjectDepartamento.get("hierarquia-organizacional"));
        
        try {
            departamentoDTO.setIdAmbienteOrganizacional((Integer) jsonObjectDepartamento.get("id-ambiente-organizacional"));
        } catch (Exception e) {
            //O construtor Integer(int) está depreciado desde a versão 9 do Java
            //departamentoDTO.setIdAmbienteOrganizacional(new Integer(0));
            departamentoDTO.setIdAmbienteOrganizacional(0);
        }
            
            try {
                departamentoDTO.setIdAreaAtuacaoUnidade((Integer) jsonObjectDepartamento.get("id-area-atuacao-unidade"));
            } catch (Exception e) {
                //O construtor Integer(int) está depreciado desde a versão 9 do Java
                //departamentoDTO.setIdAreaAtuacaoUnidade(new Integer(0));
                departamentoDTO.setIdAreaAtuacaoUnidade(0);
            }
            try {
                departamentoDTO.setIdClassificacaoUnidade((Integer) jsonObjectDepartamento.get("id-classificacao-unidade"));
            } catch (Exception e) {
                //O construtor Integer(int) está depreciado desde a versão 9 do Java
                //departamentoDTO.setIdClassificacaoUnidade(new Integer(0));
                departamentoDTO.setIdClassificacaoUnidade(0);
            }
            try {
                departamentoDTO.setIdMunicipio((Integer) jsonObjectDepartamento.get("id-municipio"));
            } catch (Exception e) {
                //O construtor Integer(int) está depreciado desde a versão 9 do Java
                //departamentoDTO.setIdMunicipio(new Integer(0));
                departamentoDTO.setIdMunicipio(0);
            }
            //departamentoDTO.setIdNivelOrganizacional((Integer) jsonObjectDepartamento.get("id-nivel-organizacional"));
            //departamentoDTO.setIdTipoUnidadeOrganizacional((Integer) jsonObjectDepartamento.get("id-tipo-unidade-organizacional"));
            departamentoDTO.setIdUnidade((Integer) jsonObjectDepartamento.get("id-unidade"));
            departamentoDTO.setIdUnidadeGestora((Integer) jsonObjectDepartamento.get("id-unidade-gestora"));
            departamentoDTO.setNomeUnidade((String) jsonObjectDepartamento.get("nome-unidade"));
            departamentoDTO.setSigla((String) jsonObjectDepartamento.get("sigla"));
            //departamentoDTO.setTelefones((String) jsonObjectDepartamento.get("telefones"));
            departamentoDTO.setUnidadePatrimonial((Boolean) jsonObjectDepartamento.get("unidade-patrimonial"));
            retorno.add(departamentoDTO);
        
        return retorno.get(0);
    }
    
}
