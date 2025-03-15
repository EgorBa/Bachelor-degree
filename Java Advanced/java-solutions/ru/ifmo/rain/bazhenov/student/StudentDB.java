package ru.ifmo.rain.bazhenov.student;

import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentGroupQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class StudentDB implements StudentGroupQuery {

    private static <R> List<R> mapStudents(List<Student> students,
                                    Function<Student, R> func) {
        return students
                .stream()
                .map(func)
                .collect(Collectors.toList());
    }

    private static List<Student> sortStudents(Collection<Student> students,
                                       Comparator<Student> comparator) {
        return students
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private static final Comparator<Student> STUDENT_NAME_COMPARATOR =
            Comparator.comparing(Student::getLastName).
                    thenComparing(Student::getFirstName).
                    thenComparingInt(Student::getId);

    private static <R> R findStudents(Collection<Student> students,
                               Predicate<Student> predicate,
                               Comparator<Student> comparator,
                               Collector<Student, ?, R> collector) {
        return students
                .stream()
                .filter(predicate)
                .sorted(comparator)
                .collect(collector);
    }

    private static List<Student> findStudentsByPredicate(Collection<Student> students,
                                                         Predicate<Student> predicate) {
        return findStudents(students, predicate, STUDENT_NAME_COMPARATOR, Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return mapStudents(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return mapStudents(students, Student::getLastName);
    }

    @Override
    public List<String> getGroups(List<Student> students) {
        return mapStudents(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return mapStudents(students, student -> String.format("%s %s", student.getFirstName(), student.getLastName()));
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.stream().map(Student::getFirstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMinStudentFirstName(List<Student> students) {
        return students
                .stream()
                .min(Comparator.comparingInt(Student::getId))
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudents(students, Comparator.comparing(Student::getId));
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudents(students, STUDENT_NAME_COMPARATOR);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentsByPredicate(students, student -> name.equals(student.getFirstName()));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentsByPredicate(students, student -> name.equals(student.getLastName()));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, String group) {
        return findStudentsByPredicate(students, student -> group.equals(student.getGroup()));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, String group) {
        return findStudents(
                students,
                student -> group.equals(student.getGroup()),
                Comparator.naturalOrder(),
                Collectors.toMap(
                        Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)
                )
        );
    }

    private static Comparator<Group> getGroupComparator(Function<Group, Integer> func) {
        return Comparator.comparing(func)
                .thenComparing(Comparator.comparing(Group::getName).reversed());
    }

    private static List<Group> getGroups(Collection<Student> students, Comparator<Student> comparator) {
        return students
                .stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet()
                .stream()
                .map((Map.Entry<String, List<Student>> e) ->
                        new Group(e.getKey(), e.getValue().stream().sorted(comparator).collect(Collectors.toList())))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }

    private static String getLargestGroupByComparator(Collection<Student> students, Comparator<Group> comparator) {
        return getGroups(students, Comparator.naturalOrder())
                .stream()
                .max(comparator)
                .map(Group::getName)
                .orElse("");
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroups(students, STUDENT_NAME_COMPARATOR);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroups(students, Comparator.comparing(Student::getId));
    }

    @Override
    public String getLargestGroup(Collection<Student> students) {
        return getLargestGroupByComparator(students, getGroupComparator((Group group) -> group.getStudents().size()));
    }

    @Override
    public String getLargestGroupFirstName(Collection<Student> students) {
        return getLargestGroupByComparator(students, getGroupComparator((Group group) ->
                (int) mapStudents(group.getStudents(), Student::getFirstName).stream().distinct().count()));
    }
}
