package br.ufrn.ct.cronos.core.utils;

import org.springframework.stereotype.Component;

import br.ufrn.ct.cronos.domain.model.Turma;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ManipuladorHorarioTurma {
    
    private static final String REGEX_HORARIO = "[2-6]+[MTN][1-6]+";
   private static final String REGEX_HORARIO_SABADO = "[2-7]+[MTN][1-6]+";
   private static final String REGEX_HORARIO_DOMINGO = "[1-7]+[MTN][1-6]+";
   private static final String REGEX_HORARIO_VALIDACAO = "[\\s]*[2-6]+[MTN][1-6]+[\\s]*([\\s]+[2-6]+[MTN][1-6]+[\\s]*)*";
   private static final String REGEX_HORARIO_SABADO_VALIDACAO = "[\\s]*[2-7]+[MTN][1-6]+[\\s]*([\\s]+[2-7]+[MTN][1-6]+[\\s]*)*";
   private static final String REGEX_HORARIO_SEM_DIAS = "[MTN][1-6]+";

   public String[] retornaArrayDias(String horarioTurma) {
      horarioTurma = horarioTurma.trim();
      StringTokenizer tokenizer = new StringTokenizer(horarioTurma);
      String stringDias = tokenizer.nextToken("[MTN]");
      String[] arrayDias = new String[stringDias.length()];
      for (int i = 0; i < stringDias.length(); i++) {
         arrayDias[i] = stringDias.substring(i, i + 1);
      }
      return arrayDias;
   }

   /*
    * Parametro: 'horarioSimples': um horario com apenas UM Dia e UM Turno. Exs.: 3M5, 4T6.
    */
   public String retornaDia(String horarioSimples) {
      horarioSimples = horarioSimples.trim();
      StringTokenizer tokenizer = new StringTokenizer(horarioSimples);
      String stringDia = tokenizer.nextToken("[MTN]");
      return stringDia;
   }

   public String retornaDiaPorIndice(String horarioTurma, int indice) {
      horarioTurma = horarioTurma.trim();
      StringTokenizer tokenizer = new StringTokenizer(horarioTurma);
      String stringDias = tokenizer.nextToken("[MTN]");
      String retorno = stringDias.substring(indice, indice + 1);
      return retorno;
   }

   public String retornaTurno(String horarioTurma) {
      horarioTurma = horarioTurma.trim();
      String turno = null;
      String[] temporario = horarioTurma.split("\\d");
      for (int i = 0; i < temporario.length; i++) {
         if (!temporario[i].equals("")) {
            turno = temporario[i];
         }
      }
      return turno;
   }

   public String[] retornaArrayHorarios(String horarioTurma) {
      horarioTurma = horarioTurma.trim();
      StringTokenizer tokenizer = new StringTokenizer(horarioTurma);
      tokenizer.nextToken("[MTN]");
      String stringHorarios = tokenizer.nextToken("[MTN]");
      String[] arrayHorarios = new String[stringHorarios.length()];
      for (int i = 0; i < stringHorarios.length(); i++) {
         arrayHorarios[i] = stringHorarios.substring(i, i + 1);
      }
      return arrayHorarios;
   }

   /*
    * horarioSimples: um horario com apenas UM Dia e UM Turno. Exs.: 3M5, 4T6.
    */
   public String retornaHorario(String horarioSimples) {
      horarioSimples = horarioSimples.trim();
      StringTokenizer tokenizer = new StringTokenizer(horarioSimples);
      tokenizer.nextToken("[MTN]");
      String stringHorario = tokenizer.nextToken("[MTN]");
      return stringHorario;
   }

   public int contadorDeGrupos(String horarioTurma) {
      Pattern p = Pattern.compile(REGEX_HORARIO);
      Matcher m = p.matcher(horarioTurma);
      int count = 0;
      while (m.find()) {
         count++;
      }
      return count;
   }

   public int contadorDeGruposComSabado(String horarioTurma) {
      Pattern p = Pattern.compile(ManipuladorHorarioTurma.REGEX_HORARIO_SABADO);
      Matcher m = p.matcher(horarioTurma);
      int count = 0;
      while (m.find()) {
         count++;
      }
      return count;
   }
   
   public int contadorDeGruposComDomingo(String horarioTurma) {
	      Pattern p = Pattern.compile(ManipuladorHorarioTurma.REGEX_HORARIO_DOMINGO);
	      Matcher m = p.matcher(horarioTurma);
	      int count = 0;
	      while (m.find()) {
	         count++;
	      }
	      return count;
	   }

   public String retornaGrupo(String horarioTurma, int indice) {
      Pattern p = Pattern.compile(REGEX_HORARIO);
      Matcher m = p.matcher(horarioTurma);
      int count = 0;
      while (m.find()) {
         if (count == indice) {
            return m.group();
         }
         count++;
      }
      return null;
   }

   public String retornaGrupoComSabado(String horarioTurma, int indice) {
      Pattern p = Pattern.compile(ManipuladorHorarioTurma.REGEX_HORARIO_SABADO);
      Matcher m = p.matcher(horarioTurma);
      int count = 0;
      while (m.find()) {
         if (count == indice) {
            return m.group();
         }
         count++;
      }
      return null;
   }
   
   public String retornaGrupoComDomingo(String horarioTurma, int indice) {
	      Pattern p = Pattern.compile(ManipuladorHorarioTurma.REGEX_HORARIO_DOMINGO);
	      Matcher m = p.matcher(horarioTurma);
	      int count = 0;
	      while (m.find()) {
	         if (count == indice) {
	            return m.group();
	         }
	         count++;
	      }
	      return null;
	   }

   public boolean validar(String horarioTurma) {
      Pattern padrao = Pattern.compile(REGEX_HORARIO_VALIDACAO);
      Matcher pesquisa = padrao.matcher(horarioTurma);
      if (pesquisa.matches()) {
         return true;
      }
      return false;
   }

   public boolean validarHorarioComSabado(String horarioTurma) {
      Pattern padrao = Pattern.compile(REGEX_HORARIO_SABADO_VALIDACAO);
      Matcher pesquisa = padrao.matcher(horarioTurma);
      if (pesquisa.matches()) {
         return true;
      }
      return false;
   }

   public boolean validarHorarioSemDias(String horarioTurma) {
      Pattern padrao = Pattern.compile(REGEX_HORARIO_SEM_DIAS);
      Matcher pesquisa = padrao.matcher(horarioTurma);
      if (pesquisa.matches()) {
         return true;
      }
      return false;
   }

   // Apenas para horarios simples, com somente um GRUPO. Ex.: 23T56
   public boolean turnosSaoIguais(String horarioTurma1, String horarioTurma2) {
      if (retornaTurno(horarioTurma1).equals(retornaTurno(horarioTurma2))) {
         return true;
      }
      return false;
   }

   // Apenas para horarios simples, com somente um GRUPO. Ex.: 23T56
   public boolean horariosSaoIguais(String horarioTurma1, String horarioTurma2) {
      String[] arrayHorarios1 = retornaArrayHorarios(horarioTurma1);
      String[] arrayHorarios2 = retornaArrayHorarios(horarioTurma2);
      if (arrayHorarios1.length != arrayHorarios2.length) {
         return false;
      }
      for (int i = 0; i < arrayHorarios1.length; i++) {
         if (!arrayHorarios1[i].equals(arrayHorarios2[i])) {
            return false;
         }
      }
      return true;
   }

   /*
    * Verifica se existe ocorrencia do parametro 'dia' no parametro string
    */
   public boolean jaExisteDia(String string, String dia) {
      string = string.trim();
      if (string.contains(dia)) {
         return true;
      }
      return false;
   }

   /*
    * Verifica se existe ocorrencia do parametro 'numeroHorario' no parametro string
    */
   public boolean jaExisteHorario(String string, String numeroHorario) {
      string = string.trim();
      if (string.contains(numeroHorario)) {
         return true;
      }
      return false;
   }

   public boolean saoConsecutivos(Turma turma1, Turma turma2) {
      int contadorDeT = contadorDeGrupos(turma2.getHorario());

      if (contadorDeT == contadorDeGrupos(turma1.getHorario())) {
         for (int h = 0; h < contadorDeGrupos(turma1.getHorario()); h++) {
            String grupo = retornaGrupo(turma1.getHorario(), h);
            String[] arrayDias = retornaArrayDias(grupo);
            String turno = retornaTurno(grupo);
            String[] arrayHorarios = retornaArrayHorarios(grupo);

            String grupoParametro = retornaGrupo(turma2.getHorario(), h);
            String[] arrayDiasParametro = retornaArrayDias(grupoParametro);
            String turnoParametro = retornaTurno(grupoParametro);
            String[] arrayHorariosParametro = retornaArrayHorarios(grupoParametro);

            if (diasIguais(arrayDias, arrayDiasParametro)) {
               if (turno.equals(turnoParametro)) {
                  if (!horarioConsecutivosTurnosIguais(arrayHorarios, arrayHorariosParametro)) {
                     return false;
                  }
               } else {
                  if ((turno.equals("M")) && (turnoParametro.equals("N"))) {
                     return false;
                  }
                  if (!horarioConsecutivosTurnosDiferentes(arrayHorarios, arrayHorariosParametro)) {
                     return false;
                  }
               }
            }
            else {
               return false;
            }
         }
      }
      else {
         return false;
      }

      return true;
   }

   public boolean diasIguais(String[] array1, String[] array2) {
      if (array1.length == array2.length) {
         for (int i = 0; i < array1.length; i++) {
            if (!array1[i].equals(array2[i]))
               return false;
         }
      }
      else {
         return false;
      }
      return true;
   }

   public boolean horarioConsecutivosTurnosIguais(String[] array1, String[] array2) {
      for (int i = 0; i < array1.length; i++) {
         if ((i == array1.length - 1) && (Integer.parseInt(array1[i]) == Integer.parseInt(array2[0]) - 1)) {
            return true;
         }
      }

      return false;
   }

   public boolean horarioConsecutivosTurnosDiferentes(String[] array1, String[] array2) {
      for (int i = 0; i < array1.length; i++) {
         if ((i == array1.length - 1) && (Integer.parseInt(array1[i]) == 6) && (Integer.parseInt(array2[0]) == 1)) {
            return true;
         }
      }

      return false;
   }

}
