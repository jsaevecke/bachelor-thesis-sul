package com.julien.saevecke.learner.sul.messages;

import com.julien.saevecke.learner.sul.proxy.DefaultQueryProxy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MembershipQuery {
    // uuid identifies the MembershipQuery
    UUID uuid;
    // query represents the query itself
    DefaultQueryProxy query;
}
