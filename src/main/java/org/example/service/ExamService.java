package org.example.service;
import java.util.*;

import org.example.model.*;
import org.example.repo.IRepository;

public class ExamService {
    private final IRepository<Exam> examRepo;

    private final IRepository<Student> studentRepo;

    private final IRepository<Teacher> teacherRepo;

    public ExamService(IRepository<Exam> examRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo) {
        this.examRepo = examRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo=teacherRepo;
    }

    public Student getStudentById(Integer studentId){
        for (Student student : studentRepo.getAll()) {
            if (student.getId().equals(studentId))
                return student;
        }
        return null;
    }

    public Teacher getTeacherById(Integer teacherId){
        for (Teacher teacher : teacherRepo.getAll()) {
            if (teacher.getId().equals(teacherId))
                return teacher;
        }
        return null;
    }

    public Exam getExamById(Integer examId){
        for (Exam exam : examRepo.getAll()) {
            if (exam.getId().equals(examId))
                return exam;
        }
        return null;
    }

    public void takeReadingExam(Integer studentId, Integer examId){
        Student student=getStudentById(studentId);

        Exam exam=getExamById(examId);
        String[][] exercises=exam.getExercises();
        Scanner scanner = new Scanner(System.in);
        String[] exercise;
        int foundCourse=0;
        float score=2;

        for (Course course:student.getCourses())
            if (course.getCourseName().contains("Reading"))
            {
                foundCourse=1;
                break;
            }

        if (foundCourse==1)
        {
            System.out.println(exercises[0][0]);
            System.out.println(exercises[1][0]);
            for (int i=2;i<6;i++)
            {
                exercise=exercises[i];
                System.out.println(exercise[0]+exercise[1]+"\n"+exercise[2]);
                System.out.println("Your answer: ");
                char answer = scanner.nextLine().charAt(0);
                int found=0;

                if (answer=='a' || answer=='b')
                {
                    for (int j=1;j<=2;j++)
                    {
                        if (exercise[j].charAt(0)==answer && exercise[j].charAt(1)=='.')
                            if (exercise[j] == exercise[3])
                            {
                                System.out.println("Correct! " + exercise[3]);
                                score+=2;
                                found=1;
                                break;
                            }
                    }
                    if (found==0)
                    {
                        System.out.println("Wrong! The right answer was " + exercise[3]);
                    }
                }
                else
                    System.out.println("Invalid choice!");
            }

            System.out.println("\n\n\nExam complete!\n\n\n");
            System.out.println("Your score: "+ score + "\n\n");
            Map<Integer, Float> readingExamResults=new HashMap<>();
            readingExamResults=student.getReadingResults();
            readingExamResults.put(examId,score);
            student.setReadingResults(readingExamResults);
            studentRepo.update(student);

            List<Student> examined;
            examined=exam.getExaminedStudents();
            examined.add(student);
            exam.setExaminedStudents(examined);
            examRepo.update(exam);

            Map<Student,Float> results;
            results=exam.getResults();
            results.put(student,score);
            exam.setResults(results);
            examRepo.update(exam);
            return;
        }

//        for (Student stud:studentRepo.getAll())
//            if (stud.getId().equals(studentId))
//                for (Course findCourse : stud.getCourses()){
//                    if (findCourse.getCourseName().contains("Reading"))
//                    {
//                        foundCourse=1;
//                        System.out.println(exercises[0][0]);
//                        System.out.println(exercises[1][0]);
//                        for (int i=2;i<6;i++)
//                        {
//                            exercise=exercises[i];
//                            System.out.println(exercise[0]+exercise[1]+"\n"+exercise[2]);
//                            System.out.println("Your answer: ");
//                            char answer = scanner.nextLine().charAt(0);
//                            int found=0;
//
//                            if (answer=='a' || answer=='b')
//                            {
//                                for (int j=1;j<=2;j++)
//                                {
//                                    if (exercise[j].charAt(0)==answer && exercise[j].charAt(1)=='.')
//                                        if (exercise[j] == exercise[3])
//                                        {
//                                            System.out.println("Correct! " + exercise[3]);
//                                            score+=2;
//                                            found=1;
//                                            break;
//                                        }
//                                }
//                                if (found==0)
//                                {
//                                    System.out.println("Wrong! The right answer was " + exercise[3]);
//                                }
//                            }
//                            else
//                                System.out.println("Invalid choice!");
//                        }
//
//                        System.out.println("\n\n\nExam complete!\n\n\n");
//                        System.out.println("Your score: "+ score + "\n\n");
//                        Map<Integer, Float> readingExamResults=new HashMap<>();
//                        readingExamResults=student.getReadingResults();
//                        readingExamResults.put(examId,score);
//                        student.setReadingResults(readingExamResults);
//
//                        List<Student> examined;
//                        examined=exam.getExaminedStudents();
//                        examined.add(student);
//                        exam.setExaminedStudents(examined);
//
//                        Map<Student,Float> results;
//                        results=exam.getResults();
//                        results.put(student,score);
//                        exam.setResults(results);
//                        return;
//                    }
//                }

        if (foundCourse==0)
            System.out.println("\n\n\nYou are not enrolled in any reading course!");
    }

    public void showReadingResults(Integer studentId){
        for (Student stud:studentRepo.getAll())
            if (stud.getId().equals(studentId)){
                Map<Integer, Float> readingExamResults=new HashMap<>();
                readingExamResults=stud.getReadingResults();
                System.out.println("Your past scores: ");
                for (Map.Entry<Integer, Float> entry : readingExamResults.entrySet()) {
                    System.out.println("Reading exam id: " + entry.getKey() + ", Score: " + entry.getValue());
                }
            }

    }



}
