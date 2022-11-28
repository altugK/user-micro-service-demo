package com.kafein.followerservice.repository;

import com.kafein.followerservice.model.Follower;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface FollowerRepository extends CassandraRepository<Follower, UUID> {
}
