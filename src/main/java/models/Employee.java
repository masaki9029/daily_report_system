package models;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = JpaConst.TABLE_EMP)
@NamedQueries({
    @NamedQuery(
            name = JpaConst.Q_EMP_GET_ALL,
            query = JpaConst.Q_EMP_GET_ALL_DEF),
    @NamedQuery(
            name = JpaConst.Q_EMP_COUNT,
            query = JpaConst.Q_EMP_COUNT_DEF),
    @NamedQuery(
            name = JpaConst.Q_EMP_COUNT_RESISTERED_BY_CODE,
            query = JpaConst.Q_EMP_COUNT_RESISTERED_BY_CODE_DEF),
    @NamedQuery(
            name = JpaConst.Q_EMP_GET_BY_CODE_AND_PASS,
            query = JpaConst.Q_EMP_GET_BY_CODE_AND_PASS_DEF)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Employee {

    @Id
    @Column(name = JpaConst.EMP_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = JpaConst.EMP_COL_CODE, nullable = false, unique = true)
    private String code;
    @Column(name = JpaConst.EMP_COL_NAME, nullable = false)
    private String name;
    @Column(name = JpaConst.EMP_COL_PASS, length = 64, nullable = false)
    private String password;
    @Column(name = JpaConst.EMP_COL_ADMIN_FLAG, nullable = false)
    private Integer adminFlag;
    @Column(name = JpaConst.EMP_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;
    @Column(name = JpaConst.EMP_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = JpaConst.EMP_COL_DELETE_FLAG, nullable = false)
    private Integer deleteFlag;



}
