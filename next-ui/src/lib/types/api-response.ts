export enum ResponseStatus {
  success = "success",
  failed = "failed",
  error = "error",
}

export interface ApiResponse<T> {
  status: ResponseStatus;
  message: string;
  data: T | null;
}
