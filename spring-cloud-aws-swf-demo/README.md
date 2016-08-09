# 
Demo project for Spring Cloud AWS with AWS Simple Workflow (SWF

Example of a simple workflow (non web) app.  
- Demonstrates basic usage of a Spring Cloud AWS
- Uses Spring Cloud AWS to obtain EC2 instance metadata when running in AWS.
- Behaves nicely when running outside of AWS
- Creates a SWF domain, workflow, and activities if needed.
-- Note: must be authorized via IAM role or IAM user/group to make SWF calls
- Runs a simple workflow (see test class) that assembles the lyrics to "Fool on the Hill".
- Contains an SWF starter, a decider, and two Workers.
