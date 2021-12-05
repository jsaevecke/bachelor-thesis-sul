package com.julien.saevecke.learner.sul.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//TODO: what does a response from a Mealy machine SUL look like?

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {
    private Query query;
    private String[] sequence;
}
