package org.example.repositories.jpa;

import org.example.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {}

//public class StudentRepository {
//    @Autowired
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    // can not use @Autowired because we have generic
//    // https://blog.csdn.net/weixin_63577740/article/details/133843898
//    @Resource
//    private RedisTemplate<String, Student> redisTemplate;
//
//    private static final String CACHE_KEY_PREFIX = "student:";
//    private static final long CACHE_EXPIRATION = 1; // 1 hour
//
//    public Student getById(Integer id) throws StudentNotFoundException {
//        String cacheKey = CACHE_KEY_PREFIX + id;
//
//        // Try to get from cache first
//        Student cachedStudent = redisTemplate.opsForValue().get(cacheKey);
//        if (cachedStudent != null) {
//            return cachedStudent;
//        }
//
//        String sql = "SELECT id, name FROM student WHERE id = :studentId";
//
//        Map<String, Integer> map = new HashMap<>();
//        map.put("studentId", id);
//
//        List<Student> rows = namedParameterJdbcTemplate.query(sql, map, new StudentRowMapper());
//
//        if (rows.isEmpty()) {
//            throw new StudentNotFoundException();
//        }
//
//        Student student = rows.getFirst();
//        // Store in cache
//        redisTemplate.opsForValue().set(cacheKey, student, CACHE_EXPIRATION, TimeUnit.HOURS);
//        return student;
//    }
//
//    public Student createStudent(String name) {
//        String sql = "INSERT INTO student (name) VALUES (:name)";
//
//        Student student = new Student();
//
//        student.setName(name);
//
//        SqlParameterSource params = new MapSqlParameterSource().addValue("name", student.getName());
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
//
//        int generatedId = keyHolder.getKey().intValue();
//        student.setId(generatedId);
//
//        String cacheKey = CACHE_KEY_PREFIX + student.getId();
//        // Store in cache
//        redisTemplate.opsForValue().set(cacheKey, student, CACHE_EXPIRATION, TimeUnit.HOURS);
//
//        return student;
//    }
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such student")  // 404
//    public class StudentNotFoundException extends RuntimeException {
//    }
//}
