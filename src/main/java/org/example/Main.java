package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.List;

import org.example.controller.*;
import org.example.repo.*;
import org.example.service.*;

import org.example.model.Student;

import org.example.model.*;
import org.example.view.StudentView;
import org.example.view.TeacherView;
import org.example.view.View;

import java.util.Map;
import java.util.HashMap;

public class Main {

    public static void basicOperations(){
        IRepository<Student> studentRepo=InFileRepository.getInstance(Student.class,"student.dat");

        Student student1=new Student("Student1",1);
        Student student2=new Student("Student2",2);
        Student student3=new Student("Student3",3);
        studentRepo.create(student1);
        studentRepo.create(student2);
        studentRepo.create(student3);


        StudentService studentService = new StudentService(studentRepo);
        for (Student student:studentRepo.getAll())
        {
            System.out.println(student.getName());
        }
        Student studentRepl=new Student("Studentrepl",1); //replaces student with id 1 with studentRepl

        studentRepo.update(studentRepl);

        for (Student student:studentRepo.getAll())
        {
            System.out.println(student.getName());
        }
    }

    public static void main(String[] args) {

        //basicOperations();

        IRepository<Student> studentRepo=InFileRepository.getInstance(Student.class,"student.dat");
        StudentService studentService = new StudentService(studentRepo);


        studentService.createStudent(1,"Student1");
        studentService.createStudent(2,"Student2");
        studentService.createStudent(3,"Student3");
        Student s1=studentService.getStudentById(1);
        s1.setName("StudentTest");
        studentRepo.update(s1);
//        for (Student student:studentRepo.getAll())
//        {
//            System.out.println(student);
//        }

//        Student temp=studentService.getStudentById(1);
//        System.out.println(temp);


        IRepository<Teacher> teacherRepo=InFileRepository.getInstance(Teacher.class,"teacher.dat");
        TeacherService teacherService=new TeacherService(teacherRepo);

        teacherService.createTeacher(1,"Teacher1");
        teacherService.createTeacher(2,"Teacher2");
        teacherService.createTeacher(3,"Teacher3");


        Teacher t1=new Teacher("Teacher4",4);
        teacherRepo.create(t1);

//        for (Teacher teacher:teacherRepo.getAll())
//        {
//            System.out.println(teacher);
//        }

        IRepository<Reading> readingRepo=InFileRepository.getInstance(Reading.class,"reading.dat");
        ReadingService readingService=new ReadingService(readingRepo,studentRepo,teacherRepo);

        Reading r1=new Reading(1,"Reading1",t1,25);
        readingRepo.create(r1);
        String[][] readingExercises = {
                {"Der Aufbruch\n" + "Franz Kafka","","",""},
                {"Ich befahl mein Pferd aus dem Stall zu holen. Der Diener verstand mich nicht.\nIch ging selbst in den Stall, sattelte mein Pferd und bestieg es. In der Ferne hörte ich eine Trompete blasen,\nich fragte ihn, was das bedeute. Er wusste nichts und hatte nichts gehört. Beim Tore hielt er mich auf und fragte:\n\"Wohin reitest du, Herr?\" \"Ich weiß es nicht,\" sagte ich, \"nur weg von hier. Immerfort weg von hier, nur so kann ich\nmein Ziel erreichen.\" \"Du kennst also dein Ziel?\" fragte er. \"Ja,\" antwortete ich, \"ich sagte es doch: »Weg-von-hier«,\ndas ist mein Ziel.\" \"Du hast keinen Essvorrat mit,\" sagte er. \"Ich brauche keinen,\" sagte ich, \"die Reise ist so lang,\ndass ich verhungern muss, wenn ich auf dem Weg nichts bekomme. Kein Essvorrat kann mich retten. Es ist ja zum Glück eine\nwahrhaft ungeheure Reise.\"", "", "", ""},
                {"\n\nDer Diener kann auf alle Fragen des Ich-Erzählers antworten.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                {"\n\nDer Ich-Erzähler nimmt einen Essvorrat.\n\n", "a. wahr", "b. falsch", "b. falsch"},
                {"\n\nDer Ich-Erzähler unternimmt eine Reise, deren Dauer undefiniert ist.\n\n", "a. wahr", "b. falsch", "a. wahr"},
                {"\n\nDie Parabel kann eine Metapher für das Unbekannte des Lebens darstellen.\n\n", "a. wahr", "b. falsch", "a. wahr"},
        };
        r1.setExercises(readingExercises);
        readingRepo.update(r1);

        readingService.enroll(1,1);
        //readingService.showEnrolledReadingCourses(1);
        //readingService.practiceReading(1,1);
        //readingService.reviewPastReadingMistakes(1);
//        readingService.showStudentsEnrolledInReadingCourses();
//        readingService.addMandatoryBook(4,1,"Die Verwandlung");
//        readingService.viewMandatoryBooks(1,1);
//        readingService.createOrUpdateReadingCourse(2,4,"Readingtemp",25,2);
        //readingService.removeCourse(2,4);
//        readingService.getAvailableCourses();

        IRepository<Grammar> grammarRepo=InFileRepository.getInstance(Grammar.class,"grammar.dat");
        GrammarService grammarService=new GrammarService(grammarRepo,studentRepo,teacherRepo);

        Grammar g1=new Grammar(10,"Grammar1",t1,25);
        grammarRepo.create(g1);
        String [][] grammarExercises={
                { "Du (brauchen) _ Hilfe.", "brauchst" },
                { "Ich bin _ Hause.", "zu" },
                { "Er trägt _.", "bei" },
                { "Diana (setzen)_ sich auf das Sofa.", "setzt" },
                { "Stefi klettert auf _ Baum.", "den" },
                { "Ich (besuchen) _ diese Kirche.", "besuche" },
                { "Wir spielen DOTA in _ Klasse.", "der" },
                { "Mama kocht immer (lecker)_ Essen", "leckeres" },
                { "Der Ball ist unter _ Tisch gerollt.", "den" },
                { "Mein Mann kommt immer betrunken _ Hause.", "nach" }
        };
        g1.setExercises(grammarExercises);
        grammarRepo.update(g1);
        grammarService.enroll(1,10);
        //grammarService.showEnrolledGrammarCourses(1);
        //grammarService.practiceGrammar(1,10);
        //grammarService.reviewPastMistakes(1,10);
        //grammarService.showStudentsEnrolledInGrammarCourses();
        //grammarService.createOrUpdateGrammarCourse(11,4,"GrammarTemp",25);
        //grammarService.removeCourse(2,4);
        //grammarService.getAvailableCourses();


        IRepository<Vocabulary> vocabRepo=InFileRepository.getInstance(Vocabulary.class,"vocabulary.dat");
        VocabularyService vocabService=new VocabularyService(vocabRepo,studentRepo,teacherRepo);

        Vocabulary v1=new Vocabulary(20,"Vocabulary1",t1,25);
        vocabRepo.create(v1);
        Map<String, String> vocabularyExercises = new HashMap<>();
        vocabularyExercises.put("Hund", "dog");
        vocabularyExercises.put("Katze", "cat");
        vocabularyExercises.put("Apfel", "apple");
        vocabularyExercises.put("Buch", "book");
        vocabularyExercises.put("Haus", "house");
        vocabularyExercises.put("Auto", "car");
        vocabularyExercises.put("Baum", "tree");
        vocabularyExercises.put("Blume", "flower");
        vocabularyExercises.put("Fisch", "fish");
        vocabularyExercises.put("Brot", "bread");
        vocabularyExercises.put("Schule", "school");

        v1.setWorter(vocabularyExercises);
        vocabRepo.update(v1);

        vocabService.enroll(1,20);
        //vocabService.showEnrolledVocabCourses(1);
        //vocabService.practiceVocabulary(1,20);
        //vocabService.reviewPastMistakes(1,20);
        //vocabService.showStudentsEnrolledInVocabCourses();
        //vocabService.createOrUpdateVocabularyCourse(21,4,"VocabularyTemp",25);
        //vocabService.removeVocabularyCourse(21,4);
        //vocabService.getAvailableCourses();

        IRepository<Writing> writingRepo=InFileRepository.getInstance(Writing.class,"writing.dat");
        WritingService writingService=new WritingService(writingRepo,studentRepo,teacherRepo);

        Writing w1=new Writing(30,"Writing1",t1,25);
        writingRepo.create(w1);
        String exercise="Schreibe einen Text über den Frühling. :3";
        w1.setRequirement(exercise);
        writingRepo.update(w1);

        writingService.enroll(1,30);
        //writingService.showEnrolledWritingCourses(1);
        //writingService.showStudentsEnrolledInWritingCourses();
        //writingService.practiceWriting(1,30);
        //writingService.gradeFeedback(4,30);
        //writingService.showFeedback(1);
//        writingService.createOrUpdateWritingCourse(31,4,"WritingTemp",25);
//        writingService.removeCourse(31,4);
//        writingService.getAvailableCourses();

        IRepository<Exam> examRepo=InFileRepository.getInstance(Exam.class,"exam.dat");
        ExamService examService=new ExamService(examRepo,studentRepo,teacherRepo);

        Exam exam1=new Exam(50,"ReadingExam1",t1);
        examRepo.create(exam1);

        exam1.setExercises(readingExercises);
        examRepo.update(exam1);

        Exam exam2=new Exam(51,"GrammarExam1",t1);
        examRepo.create(exam2);

        exam2.setExercises(grammarExercises);
        examRepo.update(exam2);

        Exam exam3=new Exam(52,"VocabExam1",t1);
        examRepo.create(exam3);

        exam3.setWorter(vocabularyExercises);
        examRepo.update(exam3);

        Exam exam4=new Exam(53,"WritingExam1",t1);
        examRepo.create(exam4);

        exam4.setRequirement(exercise);
        examRepo.update(exam4);

        //examService.takeReadingExam(1,50);
        //examService.showReadingResults(1);

        //examService.takeGrammarExam(1,51);
        //examService.showGrammarResults(1);

        //examService.takeVocabExam(1,52);
        //examService.showVocabResults(1);

        //examService.takeWritingExam(1,53);
        //examService.gradeWritings(4,53);
        //examService.showWritingResults(1);

        GrammarController grammarController = new GrammarController(grammarService);
        StudentController studentController = new StudentController(studentService);
        TeacherController teacherController = new TeacherController(teacherService);
        ReadingController readingController = new ReadingController(readingService);
        ExamController examController = new ExamController(examService);
        VocabController vocabController = new VocabController(vocabService);
        WritingController writingController = new WritingController(writingService);

        StudentView studentView = new StudentView(studentController,readingController,examController, grammarController, vocabController, writingController);
        TeacherView teacherView = new TeacherView(teacherController,readingController, writingController,vocabController,grammarController,examController);

        View view = new View(studentView,teacherView);
        view.start();


    }


}
