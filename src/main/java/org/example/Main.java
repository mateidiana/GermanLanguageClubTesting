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
        for (Student student:studentRepo.getAll())
        {
            System.out.println(student);
        }

//        Student temp=studentService.getStudentById(1);
//        System.out.println(temp);


        IRepository<Teacher> teacherRepo=InFileRepository.getInstance(Teacher.class,"teacher.dat");
        TeacherService teacherService=new TeacherService(teacherRepo);

        teacherService.createTeacher(1,"Teacher1");
        teacherService.createTeacher(2,"Teacher2");
        teacherService.createTeacher(3,"Teacher3");
        for (Teacher teacher:teacherRepo.getAll())
        {
            System.out.println(teacher);
        }




    }


}
