package com.demo.base;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AxiosGenerator {
    //Specify Package Name
    private final static String basePackage = "com";

    private final static ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
        {
            scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        }
    };

    public static void main(String[] args) throws Exception {
        StringBuilder output = new StringBuilder(AXIOS_SINGLETON);
        output.append("\n\n// =================> Generated API <=================\n\n");

        Pattern pathVarPattern = Pattern.compile("\\{([^/]+)}");

        for (var beanDefinition : scanner.findCandidateComponents(basePackage)) {
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                String[] paths = clazz.getAnnotation(RequestMapping.class).value();
                if (paths.length > 0) {
                    baseUrl = paths[0];
                }
            }

            for (Method method : clazz.getDeclaredMethods()) {
                String methodUrl = "";
                String httpMethod = "";

                if (method.isAnnotationPresent(GetMapping.class)) {
                    methodUrl = getFirstPath(method.getAnnotation(GetMapping.class).value());
                    httpMethod = "GET";
                } else if (method.isAnnotationPresent(PostMapping.class)) {
                    methodUrl = getFirstPath(method.getAnnotation(PostMapping.class).value());
                    httpMethod = "POST";
                } else if (method.isAnnotationPresent(PutMapping.class)) {
                    methodUrl = getFirstPath(method.getAnnotation(PutMapping.class).value());
                    httpMethod = "PUT";
                } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                    methodUrl = getFirstPath(method.getAnnotation(DeleteMapping.class).value());
                    httpMethod = "DELETE";
                } else if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    methodUrl = getFirstPath(mapping.value());
                    httpMethod = mapping.method().length > 0 ? mapping.method()[0].name() : "GET";
                }

                if (!httpMethod.isEmpty()) {
                    String fullPath = normalizePath(baseUrl + methodUrl);
                    String apiName = method.getName();

                    Matcher matcher = pathVarPattern.matcher(fullPath);
                    List<String> pathVars = new ArrayList<>();
                    StringBuilder urlBuffer = new StringBuilder();

                    while (matcher.find()) {
                        String varName = matcher.group(1);
                        pathVars.add(varName);

                        matcher.appendReplacement(urlBuffer, "\\$\\{parameter." + varName + "\\}");
                    }
                    matcher.appendTail(urlBuffer);

                    String paramType = "Record<string, any>";
                    String finalUrlStr;

                    if (pathVars.isEmpty()) {
                        finalUrlStr = "'" + fullPath + "'";
                    } else {
                        finalUrlStr = "`" + urlBuffer + "`";

                        StringBuilder typeBuilder = new StringBuilder("Record<string, any> & { ");
                        for (int i = 0; i < pathVars.size(); i++) {
                            typeBuilder.append(pathVars.get(i)).append(": any");
                            if (i < pathVars.size() - 1) typeBuilder.append(", ");
                        }
                        typeBuilder.append(" }");
                        paramType = typeBuilder.toString();
                    }

                    String generatedCode = API_PROTOTYPE
                            .replace("${API_NAME}", apiName)
                            .replace("${API_PARAM_TYPE}", paramType)
                            .replace("${API_URL}", finalUrlStr)
                            .replace("${API_METHOD}", httpMethod);

                    output.append(generatedCode).append("\n");
                }
            }
        }

        Path path = Paths.get("generated-axios-api.tsx");

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Files.writeString(path, output.toString(), StandardCharsets.UTF_8);

            System.out.println("成功生成文件至: " + path.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("写入文件失败: " + e.getMessage());
        }
    }

    private static String getFirstPath(String[] paths) {
        return paths.length > 0 ? paths[0] : "";
    }

    private static String normalizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path.replaceAll("/+", "/");
    }


    private static final String AXIOS_SINGLETON = """
            /* eslint-disable */
            import type {AxiosInstance, AxiosResponse, InternalAxiosRequestConfig} from 'axios'
            import axios from 'axios'
            import { ref, type Ref } from 'vue'
            
            const instance: AxiosInstance = axios.create({baseURL: import.meta.env.DEV ? '/api' : '/', timeout: 10000})
            
            instance.interceptors.request.use(
                (config: InternalAxiosRequestConfig) => {
                    config.withCredentials = true
                    return config
                },
                (error) => {
                    return Promise.reject(error);
                }
            )
            
            instance.interceptors.response.use(
                (response: AxiosResponse): AxiosResponse => {
                    return response
                },
                (error) => {
                    return Promise.reject(error)
                }
            )
            
            export const AxiosExchange = <T = any>({
                                                       url,
                                                       method = 'GET' as 'GET' | 'POST' | 'PUT' | 'DELETE',
                                                       parameter = {},
                                                       header = {},
                                                       timeout,
                                                   }: {
                url: string;
                method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
                parameter?: Record<string, any>;
                header?: Record<string, any>;
                timeout?: number;
            }) => {
                const isGet = method.toUpperCase() === 'GET';
                const isLoading = ref(false);
                const request: Promise<AxiosResponse<T>> = instance({
                    url,
                    method: method.toUpperCase(),
                    params: isGet ? parameter : undefined,
                    data: !isGet ? parameter : undefined,
                    headers: header,
                    timeout: timeout || instance.defaults.timeout,
                });
                isLoading.value = true;
                request
                    .finally(() => {
                        isLoading.value = false;
                    });
            
                return {
                    isLoading,
                    response: request,
                }
            };
            """;

    private static final String API_PROTOTYPE = """
            export const ${API_NAME} = (parameter: ${API_PARAM_TYPE}): { isLoading: Ref<boolean>, response: Promise<any> } => {
                return AxiosExchange<any>({
                    url: ${API_URL},
                    method: '${API_METHOD}',
                    parameter: parameter
                })
            }
            """;
}