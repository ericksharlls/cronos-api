package br.ufrn.ct.cronos.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class ManipuladorDatas {

    public static int retornaDiaDaSemana(Date data) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        return gc.get(GregorianCalendar.DAY_OF_WEEK);
     }
  
     public static List<Date> retornaDatasSemSabadosDomingos(List<Date> datas) {
        List<Date> retorno = new ArrayList<Date>(0);
        GregorianCalendar gc = new GregorianCalendar();
        for (Date data : datas) {
           gc.setTime(data);
           if (gc.get(GregorianCalendar.DAY_OF_WEEK) != 1 && gc.get(GregorianCalendar.DAY_OF_WEEK) != 7) {
              retorno.add(data);
           }
        }
        return retorno;
     }
  
     /*
      * Retorna o parametro 'datas' sem os dias correspodentes aos sabados, domingos e feriados.
      */
     public static List<Date> retornaDatasSemSabadosDomingosFeriados(List<Date> datas, List<Date> feriados) {
        List<Date> retorno = new ArrayList<Date>(0);
        GregorianCalendar gc = new GregorianCalendar();
        for (Iterator<Date> iterator = datas.iterator(); iterator.hasNext();) {
           Date date = (Date) iterator.next();
           if (feriados.contains(date)) {
              iterator.remove();
           }
        }
        for (Date data : datas) {
           gc.setTime(data);
           if (gc.get(GregorianCalendar.DAY_OF_WEEK) != 1 && gc.get(GregorianCalendar.DAY_OF_WEEK) != 7) {
              retorno.add(data);
           }
        }
        return retorno;
     }
  
     public static String retornaListaDiasDaSemana(List<Date> datas) {
        List<String> listaDiasDaSemana = new ArrayList<String>(0);
        for (Date data : datas) {
           GregorianCalendar gc = new GregorianCalendar();
           gc.setTime(data);
           if (!listaDiasDaSemana.contains(String.valueOf(gc.get(GregorianCalendar.DAY_OF_WEEK)))) {
              listaDiasDaSemana.add(String.valueOf(gc.get(GregorianCalendar.DAY_OF_WEEK)));
           }
        }
        String diasDaSemana = "";
        for (String string : listaDiasDaSemana) {
           diasDaSemana = diasDaSemana + string;
        }
        return diasDaSemana;
     }
  
     /*
      * Retorna o parametro 'intervaloDatas' sem os dias correspodentes aos domingos e feriados, e tambem sem os dias q nao corresponderem aos
      * dias do horario da turma passado no parametro 'horarioTurma'
      */
     public static List<Date> retornaDatasLetivasPorDias(List<Date> intervaloDatas, List<Date> feriados,
        String horarioTurma) {
        List<Date> retorno = new ArrayList<Date>(0);
        GregorianCalendar gc = new GregorianCalendar();
  
        for (Iterator<Date> iterator = intervaloDatas.iterator(); iterator.hasNext();) {
           Date data = (Date) iterator.next();
           if (feriados.contains(data)) {
              iterator.remove();
           }
        }
  
        for (Date data : intervaloDatas) {
           gc.setTime(data);
           String[] arrayDias = retornaArrayDias(horarioTurma);
           for (int i = 0; i < arrayDias.length; i++) {
              if (gc.get(GregorianCalendar.DAY_OF_WEEK) != 1 && gc.get(GregorianCalendar.DAY_OF_WEEK) == Integer.parseInt(arrayDias[i])) {
                 retorno.add(data);
                 break;
              }
           }
        }
        return retorno;
     }
  
     public static String[] retornaArrayDias(String horarioTurma) {
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
      * Retorna o parametro 'intervaloDatas' sem os dias correspodentes aos feriados, e tambem sem os dias q nao corresponderem aos dias do
      * horario da turma passado no parametro 'horarioTurma'
      */
     public static List<Date> retornaDatasPorDiasSemFeriados(List<Date> intervaloDatas, List<Date> feriados,
        String horarioTurma) {
        List<Date> retorno = new ArrayList<Date>(0);
        GregorianCalendar gc = new GregorianCalendar();
  
        for (Iterator<Date> iterator = intervaloDatas.iterator(); iterator.hasNext();) {
           Date data = (Date) iterator.next();
           if (feriados.contains(data)) {
              iterator.remove();
           }
        }
  
        for (Date data : intervaloDatas) {
           gc.setTime(data);
           String[] arrayDias = retornaArrayDias(horarioTurma);
           for (int i = 0; i < arrayDias.length; i++) {
              if (gc.get(GregorianCalendar.DAY_OF_WEEK) == Integer.parseInt(arrayDias[i])) {
                 retorno.add(data);
                 break;
              }
           }
        }
        return retorno;
     }
  
     /*
      * Retorna o parametro 'intervaloDatas' sem os dias q nao corresponderem aos dias do horario da turma passado no parametro 'horarioTurma'
      */
     public static List<Date> retornaDatasPorDias(List<Date> intervaloDatas, String horarioTurma) {
        List<Date> retorno = new ArrayList<Date>(0);
        GregorianCalendar gc = new GregorianCalendar();
  
        for (Date data : intervaloDatas) {
           gc.setTime(data);
           String[] arrayDias = retornaArrayDias(horarioTurma);
           for (int i = 0; i < arrayDias.length; i++) {
              if (gc.get(GregorianCalendar.DAY_OF_WEEK) == Integer.parseInt(arrayDias[i])) {
                 retorno.add(data);
                 break;
              }
           }
        }
        return retorno;
     }
  
     /*
      * xxxx
      */
     public static String retornaStringDataFormatoDiaMesAno(Date data) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(data);
     }
  
     /*
      * xxxx
      */
     public static String retornaStringDataFormatoHoraMinutoSegundo(Date data) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(data);
     }

}
