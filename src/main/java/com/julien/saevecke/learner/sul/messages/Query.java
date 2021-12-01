package com.julien.saevecke.learner.sul.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//TODO: what does a query to a Mealy machine SUL look like?

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Query {
    private String input;
}
