package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.List;

import org.example.repo.*;
import org.example.service.*;

import org.example.model.Student;

import org.example.model.*;
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
        readingService.showEnrolledReadingCourses(1);
        //readingService.practiceReading(1,1);
        //readingService.reviewPastReadingMistakes(1);
//        readingService.showStudentsEnrolledInReadingCourses();
//        readingService.addMandatoryBook(4,1,"Die Verwandlung");
//        readingService.viewMandatoryBooks(1,1);
//        readingService.createOrUpdateReadingCourse(2,4,"Readingtemp",25,2);
//        readingService.getAvailableCourses();


    }


}
