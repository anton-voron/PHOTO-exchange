package com.voron.petproject.amazon_bucket;

public enum BucketName {
	PROFILE_IMAGE("voron-image-upload");

	private final String bucketName;

	BucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketName() {
		return bucketName;
	}
}
