import Exceptions.InvalidParameterException;
import Exceptions.InvalidUrlException;
import Objects.LiftRideDetail;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import utilities.JsonUtilities;
import Objects.LiftRide;
import utilities.NumberUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkierServlet extends HttpServlet {

    public void init() throws ServletException {
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        String urlPath = req.getPathInfo();
        Map<String, String> responseBody = new HashMap<>();
        try {
            Map<String, String> parameters = getParametersFromUrl(urlPath);
            String requestData = req.getReader().lines().collect(Collectors.joining());
            LiftRide liftRide = new Gson().fromJson(requestData, LiftRide.class);
            validateLiftRide(liftRide);
            LiftRideDetail detail = createLiftRideDetail(parameters, liftRide);
            responseBody.put("message", "created details");
            resp.setStatus(201);
            resp.getWriter().write(JsonUtilities.toJsonString(responseBody));
        } catch (InvalidUrlException e) {
            responseBody.put("message", "Invalid url! Resource not found!");
            resp.setStatus(404);
            resp.getWriter().write(JsonUtilities.toJsonString(responseBody));
        } catch (InvalidParameterException | JsonParseException e) {
            responseBody.put("message", e.getMessage());
            resp.setStatus(400);
            resp.getWriter().write(JsonUtilities.toJsonString(responseBody));
        }
    }

    private LiftRideDetail createLiftRideDetail(Map<String, String> parameters, LiftRide liftRide) {
        LiftRideDetail detail = new LiftRideDetail(Integer.valueOf(parameters.get("resortId")),
                                parameters.get("seasonId"),
                                parameters.get("dayId"),
                                parameters.get("skierId"),
                                liftRide);
        /*
            In the future, if you have a database you can write the code to insert the lift ride details
            into a table
         */
        return detail;
    }
    private Map<String, String> getParametersFromUrl(String urlPath)
            throws InvalidUrlException, InvalidParameterException {
        String[] parts = urlPath.split("/");
        Map<String, String> parameters = new HashMap<>();
        validateUrl(parts);

        parameters.put("resortId", parts[1]);
        parameters.put("seasonId", parts[3]);
        parameters.put("dayId", parts[5]);
        parameters.put("skierId", parts[6]);
        return parameters;
    }
    private void validateLiftRide(LiftRide liftRide) throws InvalidParameterException {
        if (liftRide == null) throw new InvalidParameterException("LiftRide object is not valid");
        if (liftRide.getLiftID() == null) throw new InvalidParameterException("Lift id is invalid");
        if (liftRide.getTime() == null) throw new InvalidParameterException("Time is invalid");
    }
    private void validateUrl(String[] parts) throws InvalidUrlException, InvalidParameterException {
        // this method is only specific to the POST API
        if(parts.length != 8) throw new InvalidUrlException();
        if(!NumberUtilities.isAnInteger(parts[1])) throw new InvalidParameterException("ResortId is not an integer");
        if(!parts[2].equals("seasons")) throw new InvalidUrlException();
        if(!NumberUtilities.isAnInteger(parts[3])) throw new InvalidParameterException("SeasonId is not an integer");
        if(!parts[4].equals("days")) throw new InvalidUrlException();
        if(!NumberUtilities.isAnInteger(parts[5])) throw new InvalidParameterException("dayId is not an integer");
        if(!NumberUtilities.isBetween(parts[5], 1, 366)) throw new InvalidParameterException("dayId is not within range");
        if(!parts[6].equals("skiers")) throw new InvalidUrlException();
        if(!NumberUtilities.isAnInteger(parts[7])) throw new InvalidParameterException("skierId is not an integer");
    }
}
