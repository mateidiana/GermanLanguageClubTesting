package org.example.service;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.example.model.Course;
import org.example.model.Student;
import org.example.model.Teacher;
import org.example.repo.*;

import org.example.model.*;
public class WritingService {
    private final IRepository<Writing> writingRepo;
    private final IRepository<Student> studentRepo;
    private final IRepository<Teacher> teacherRepo;

    public WritingService(IRepository<Writing> writingRepo, IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo) {
        this.writingRepo = writingRepo;
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

    public Writing getWritingById(Integer writingId){
        for (Writing writing : writingRepo.getAll()) {
            if (writing.getId().equals(writingId))
                return writing;
        }
        return null;
    }

    public void enroll(Integer studentId, Integer writingCourseId) {
        int alreadyEnrolled=0;

        Student student = getStudentById(studentId);
        Writing course = getWritingById(writingCourseId);

        for (Course course1:student.getCourses()){
            if (course1.getId().equals(writingCourseId))
                alreadyEnrolled=1;
        }

        if (alreadyEnrolled==0){
            studentRepo.delete(studentId);
            writingRepo.delete(writingCourseId);
            if (course.getAvailableSlots() > course.getEnrolledStudents().size()) {
                course.getEnrolledStudents().add(student);
                student.getCourses().add(course);
                writingRepo.create(course);
                studentRepo.create(student);
            }
        }

    }

    public void practiceWriting(Integer studentId, Integer courseId) {

        Student student = getStudentById(studentId);
        Writing course = getWritingById(courseId);
        Teacher teacher=course.getTeacher();
        Scanner scanner = new Scanner(System.in);
        StringBuilder answer = new StringBuilder();

        int foundCourse=0;

        for (Course findCourse : student.getCourses()){
            if (findCourse.getId().equals(courseId))
            {
                foundCourse=1;
                break;}
        }
        if (foundCourse==0){
            System.out.println("\n\n\nYou are not enrolled in this course!");}
        if(foundCourse==1) {
            String exercise=course.getRequirement();
            System.out.println(exercise);
            String input;


            System.out.println("Enter text (type 0 to stop):");
            while (true) {
                input = scanner.nextLine();
                if (input.equals("0")) {
                    System.out.println("Exiting...");
                    break;
                }
                answer.append(input).append("\n");
            }
            Map <Student, String> toBeGraded=course.getTeacher().getFeedbackWriting();
            toBeGraded.put(student, answer.toString());
            teacher.setFeedbackWriting(toBeGraded);
            teacherRepo.update(teacher);
            System.out.println(course.getTeacher().getFeedbackWriting());
            System.out.println("Writing exercise submitted!!!!!");
        }

    }

    public List<Student> getAllStudents() {
        return studentRepo.getAll();
    }

    public void getAvailableCourses() {
        for (Writing writing:writingRepo.getAll())
            System.out.println(writing);
    }

    public List<Student> getEnrolledStudents(Integer courseId) {
        //Writing course = writingRepo.getById(courseId);

        Writing course = getWritingById(courseId);
        return course.getEnrolledStudents();
    }

    public void showEnrolledWritingCourses(Integer studentId){
        Student student=getStudentById(studentId);
        for (Course course:student.getCourses())
            if (course.getCourseName().contains("Writing"))
                System.out.println(course);
    }

    public void viewCourseTaughtByTeacher(Integer teacherId){
        for(Writing course:writingRepo.getAll())
            if (course.getTeacher().getId().equals(teacherId))
                System.out.println(course.getCourseName());
    }

    public void showStudentsEnrolledInWritingCourses(){
        for(Student student:studentRepo.getAll())
            for(Course course:student.getCourses())
                if(course.getCourseName().contains("Writing"))
                {
                    System.out.println(student);
                    break;
                }
    }

    public void createOrUpdateWritingCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {
        int found = 0;
        for (Writing course : writingRepo.getAll()) {
            if (course.getId().equals(courseId)) {
                found = 1;
                updateWritingCourse(courseId, teacherId, courseName, maxStudents);
                return;
            }
        }
        if (found == 0) {
            createWritingCourse(courseId, teacherId, courseName, maxStudents);
        }
    }


    public void createWritingCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {

        Teacher teacher = getTeacherById(teacherId);
        Writing w1 = new Writing(courseId, courseName, teacher, maxStudents);
        writingRepo.create(w1);
        String exercise="Schreibe einen Text über den Frühling. :3";
        w1.setRequirement(exercise);
        writingRepo.update(w1);
    }

    public void updateWritingCourse(Integer courseId, Integer teacherId, String courseName, Integer maxStudents) {

        Writing course = getWritingById(courseId);

        Teacher teacher = getTeacherById(teacherId);
        writingRepo.delete(courseId);
        Writing w1 = new Writing(courseId, courseName, teacher, maxStudents);
        writingRepo.create(w1);
        String exercise="Schreibe einen Text über den Frühling. :3";
        w1.setRequirement(exercise);
        writingRepo.update(w1);
    }

    /**
     * A teacher can remove a writing course
     * @param courseId Refers to a specific course
     * @param teacherId Refers to the teacher who removes the course
     */
    public void removeCourse(Integer courseId, Integer teacherId) {
        Writing course = getWritingById(courseId);
        if (course.getTeacher().getId().equals(teacherId)) {
            writingRepo.delete(courseId);
        } else {
            System.out.println("You don't have access to this course!");
        }
    }

    /**
     * Shows the teacher's feedback on a student's writing practice
     * @param studentId identifies a student
     */
    public void showFeedback(Integer studentId){

        Student student=getStudentById(studentId);
        Map<Integer, Float> writingFeedback=new HashMap<>();
        writingFeedback=student.getWritingFeedback();
        System.out.println("Your past scores: ");
        for (Map.Entry<Integer, Float> entry : writingFeedback.entrySet()) {
            System.out.println("Writing exam id: " + entry.getKey() + ", Score: " + entry.getValue());
        }
    }

    /**
     * A teacher can give a feedback for every student who practiced their writing course
     * @param teacherId identifies a teacher
     * @param courseId identifies a course
     */
    public void gradeFeedback(Integer teacherId, Integer courseId){
        Teacher teacher= getTeacherById(teacherId);
        Writing course= getWritingById(courseId);

        Scanner scanner=new Scanner(System.in);
        Map<Student, String> toGrade=teacher.getFeedbackWriting();
        System.out.println(toGrade);
        System.out.println(course.getTeacher().getFeedbackWriting());
        while (!toGrade.isEmpty()) {
            Map.Entry<Student, String> entry = toGrade.entrySet().iterator().next();
            Student key = entry.getKey();
            String value = entry.getValue();
            System.out.println(value);
            System.out.println("Input grade: ");
            float grade=scanner.nextFloat();
            Map<Integer, Float> results=key.getWritingFeedback();
            results.put(courseId, grade);
            key.setWritingFeedback(results);
            studentRepo.update(key);
            toGrade.remove(key);
        }
        System.out.println();
    }

}
