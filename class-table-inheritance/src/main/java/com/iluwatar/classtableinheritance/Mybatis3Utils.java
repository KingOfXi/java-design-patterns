package com.iluwatar.classtableinheritance;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * This is a tool class which use mybatis and mysql .
 *
 * @author ZhangXiZhi
 */
@Slf4j
public final class Mybatis3Utils {

  /**
   * It can be extended in the future, it can be thread.
   */
  public static final ThreadLocal<SqlSession> LOCAL
      = new ThreadLocal<>();
  /**
   * this is a factory field.
   */
  private static  SqlSessionFactory sqlSessionFactory;

  /**
   * getter factory.
   *
   * @return sql factory.
   */
  public static SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  /**
   * constructor.
   */
  private Mybatis3Utils() {
  }



  static {
    try {
      final Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    } catch (IOException e) {
      LOGGER.info("mybatis import error");
    }
  }

  /**
   * Get related session.
   *
   * @return sqlsession can deal with some requests.
   */
  public static SqlSession getCurrentSqlSession() {
    SqlSession sqlSession = LOCAL.get();
    if (Objects.isNull(sqlSession)) {
      sqlSession = sqlSessionFactory.openSession();
      LOCAL.set(sqlSession);
    }
    return sqlSession;
  }

  /**
   * Close the session.
   */
  public static void closeCurrentSession() {
    final SqlSession sqlSession = LOCAL.get();
    if (Objects.nonNull(sqlSession)) {
      sqlSession.close();
    }
    LOCAL.set(null);
  }
}
