import Axios, { AxiosRequestConfig } from 'axios';

export const AXIOS_INSTANCE = Axios.create({
  baseURL: typeof window === 'undefined'
    ? process.env.NEXT_PUBLIC_API_BASE_URL // When on the server, use the docker-resolvable URL
    : '', // When in the browser, use a relative URL so that the browser uses the current origin
});

 export const axiosInstance = <T>(
    config: AxiosRequestConfig,
    options?: AxiosRequestConfig,
  ): Promise<T> => {
    const source = Axios.CancelToken.source();
    const promise = AXIOS_INSTANCE({
      ...config,
      ...options,
      cancelToken: source.token,
    }).then(({ data }) => data);
    // @ts-expect-error: need to write something here
    promise.cancel = () => {
      source.cancel('Query was cancelled');
    };
    return promise;
  };