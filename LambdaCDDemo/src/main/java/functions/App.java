package functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, Object> {
    String output;

    public Object handleRequest(final Object input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        try {
            final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

// snippet-start:[ec2.java1.describe_region_and_zones.regions]
            DescribeRegionsResult regions_response = ec2.describeRegions();
            List<String> outputList = new ArrayList<>();
            for (Region region : regions_response.getRegions()) {
                output = String.format("Found region %s " +
                                "with endpoint %s",
                        region.getRegionName(),
                        region.getEndpoint());
                outputList.add(output);
            }
                return new GatewayResponse(output, headers, 200);

        } catch (Exception e) {
            return new GatewayResponse("{}", headers, 500);
        }

    }
}
