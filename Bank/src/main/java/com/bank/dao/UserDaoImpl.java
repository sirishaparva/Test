package com.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bank.beans.Funds;
import com.bank.beans.Login;
import com.bank.beans.TransferRequest;
import com.bank.beans.User;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Integer createUser(User user) {

		String userSql = "INSERT INTO USER(USER_NAME,MOBILE_NO,ACCOUNT_NO) values(?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getUserName());
				ps.setInt(2, user.getMobileNo());
				ps.setInt(3, user.getAccountNo());
				return ps;
			}
		}, keyHolder);

		Integer userId = (Integer) keyHolder.getKey().intValue();

		if (userId != null) {
			user.getLogin().setUserId(userId);
			saveLoginDetails(user.getLogin());

			user.getFunds().setUserId(userId);
			saveFunds(user.getFunds(), user.getAccountNo());
		}

		return user.getAccountNo();

	}

	@Override
	public List<Map<String, Object>> getUsers() {

		List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from user");
		return rows;
	}

	@Override
	public Login userLogin(Login login) {
		Login response = new Login();
		String sqlLogin = "SELECT user_id as userId FROM user_login WHERE user_name = ? AND PASSWORD = ?";
		Map<String, Object> row = jdbcTemplate.queryForMap(sqlLogin,login.getUserName(),login.getPassWord());
		if (!row.isEmpty()) {
		response.setUserId((Integer)row.get("userId"));
		}
		return response;
	}

	@Override
	public String transferFunds(TransferRequest tranfer) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ACCOUNT_NO", tranfer.getToAcc());
		String sql = " SELECT usr.USER_ID as userId,fund.FUNDS_ID as fundId,fund.BALANCE as amount FROM user usr JOIN funds fund ON usr.USER_ID = fund.USER_ID WHERE usr.ACCOUNT_NO = ?";
		Map<String, Object> toFunds = jdbcTemplate.queryForMap(sql, new Object[] { tranfer.getToAcc() });
		if (!toFunds.isEmpty()) {
			params.addValue("ACCOUNT_NO", tranfer.getFromAcc());
			Map<String, Object> fromFunds = jdbcTemplate.queryForMap(sql, new Object[] { tranfer.getFromAcc() });
			if (!fromFunds.isEmpty()) {
				Double toBalance = (Double) toFunds.get("amount");
				Double fromBalance = (Double) fromFunds.get("amount");

				System.out.println(toBalance + " " + fromBalance);

				if (Double.compare(fromBalance, tranfer.getTransferAmount()) > 0) {
					double amount = fromBalance - tranfer.getTransferAmount();
					double totalAmount = toBalance + tranfer.getTransferAmount();

					String updateFromAcc = "update funds set BALANCE = ? where funds_id = ?";

					jdbcTemplate.update(updateFromAcc, amount, fromFunds.get("fundId"));
					jdbcTemplate.update(updateFromAcc, totalAmount, toFunds.get("fundId"));
				} else {
					return "Insufficent funds";
				}

			}

		} else {

		}
		return sql;

	}

	private Integer saveLoginDetails(Login login) {

		String sqlLogin = "INSERT INTO USER_LOGIN (USER_ID,PASSWORD,USER_NAME) VALUES(?,?,?)";

		return jdbcTemplate.update(sqlLogin, login.getUserId(), login.getPassWord(), login.getUserName());

	}

	private Integer saveFunds(Funds funds, Integer accNo) {

		String sqlFunds = "INSERT INTO FUNDS (BALANCE,USER_ID) VALUES(?,?)";

		return jdbcTemplate.update(sqlFunds, funds.getAmount(), funds.getUserId());
	}

}
