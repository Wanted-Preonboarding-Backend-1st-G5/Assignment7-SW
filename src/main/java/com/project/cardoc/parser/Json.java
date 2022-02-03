package com.project.cardoc.parser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Json {

  public static JSONObject stringToJsonObject(final String content) throws ParseException {
    JSONParser jsonParser = new JSONParser();
    return (JSONObject) jsonParser.parse(content);
  }
}
