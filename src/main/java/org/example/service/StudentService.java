package org.example.service;
import org.example.model.Student;
import org.example.repo.IRepository;


public class StudentService {

    /**
     * The repository used to access and manipulate {@link Student} objects.
     */
    //private StudentRepository studentRepo;
    private final IRepository<Student> studentRepo;


//    public StudentService(StudentRepository studentRepo){
//        this.studentRepo=studentRepo;
//    }

    public StudentService(IRepository<Student> studentRepo){
        this.studentRepo=studentRepo;
    }
    /**
     * Creates a new {@link Student} if the provided student ID is not already in use.
     * It checks if the student ID exists in the repository and if not, saves the new student.
     *
     * @param studentId The ID of the student to be created.
     * @param name The name of the student to be created.
     */
    public void createStudent(Integer studentId, String name){
        for(Student student:studentRepo.getAll())
            if (student.getId().equals(studentId)){
                System.out.println("Id already in use!");
                return;
            }

        Student student = new Student(name,studentId);
        studentRepo.create(student);
        System.out.println("Registration successful!");
    }

    public Student getStudentById(Integer studentId){
        for (Student student : studentRepo.getAll()) {
            if (student.getId().equals(studentId))
                return student;
        }
        return null;
    }
}
