package com.bank.beans;

public class TransferRequest {

	private Integer toAcc;

	private Integer fromAcc;

	private long transferAmount;

	private Integer toUserId;

	private Integer fromUserId;

	public Integer getToAcc() {
		return toAcc;
	}

	public void setToAcc(Integer toAcc) {
		this.toAcc = toAcc;
	}

	public Integer getFromAcc() {
		return fromAcc;
	}

	public void setFromAcc(Integer fromAcc) {
		this.fromAcc = fromAcc;
	}

	public long getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(long transferAmount) {
		this.transferAmount = transferAmount;
	}

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

}
