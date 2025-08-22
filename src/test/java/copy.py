import os

# List of directories to search
directories = ['model', 'view', 'controller']
output_file = 'combined_java_code.txt'

def collect_java_files(directories):
    java_files = []
    for directory in directories:
        # Check if the directory exists
        if os.path.isdir(directory):
            # List files in the directory
            for filename in os.listdir(directory):
                # Only consider files ending with .java
                if filename.endswith('.java'):
                    file_path = os.path.join(directory, filename)
                    java_files.append(file_path)
        else:
            print(f"Directory '{directory}' does not exist.")
    return java_files

def write_combined_file(java_files, output_file):
    with open(output_file, 'w', encoding='utf-8') as outfile:
        for file_path in java_files:
            # Write a header for each file
            outfile.write(f"\n\n--- Start of file: {file_path} ---\n\n")
            try:
                with open(file_path, 'r', encoding='utf-8') as infile:
                    outfile.write(infile.read())
            except Exception as e:
                outfile.write(f"Error reading {file_path}: {e}\n")
            outfile.write(f"\n--- End of file: {file_path} ---\n")
    print(f"Combined Java code has been saved to '{output_file}'.")

if __name__ == '__main__':
    java_files = collect_java_files(directories)
    if java_files:
        write_combined_file(java_files, output_file)
    else:
        print("No Java files found in the specified directories.")
